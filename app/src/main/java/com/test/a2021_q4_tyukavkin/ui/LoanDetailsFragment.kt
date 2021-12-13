package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanDetailsBinding
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanDetailsFragmentViewModel
import javax.inject.Inject

class LoanDetailsFragment : Fragment() {

    private var _binding: FragmentLoanDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanDetailsFragmentViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[LoanDetailsFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.loanId.value == null) {
            arguments?.let {
                val loanId = it.getLong("ID")
                viewModel.setLoanId(loanId)
                viewModel.getLoanData(loanId)
            }
        }

        viewModel.apply {

            loan.observe(viewLifecycleOwner, { loan ->
                binding.apply {
                    loanRequestNumber.append(loan.id.toString())
                    loanRequestStatus.append(loan.state)
                    borrowerName.append("${loan.firstName} ${loan.lastName}")
                    borrowerPhoneNumber.append(loan.phoneNumber)
                    loanAmount.append(loan.amount.toString())
                    loanPercent.append("${loan.percent}%")
                    loanPeriod.append(loan.period.toString())
                    loanDate.append("${loan.date} ${loan.time}")
                }
            })

            state.observe(viewLifecycleOwner, { state ->
                when (state) {
                    FragmentState.UNKNOWN_HOST ->
                        showError(
                            getString(R.string.unknown_host_exception_msg),
                            getString(R.string.refresh)
                        ) {
                            arguments?.let { viewModel.getLoanData(it.getLong("ID")) }
                        }
                    FragmentState.TIMEOUT ->
                        showError(
                            getString(R.string.timeout_exception_msg),
                            getString(R.string.refresh)
                        ) {
                            arguments?.let { viewModel.getLoanData(it.getLong("ID")) }
                        }
                    else -> updateUI(state)
                }
            })


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        errorSnackbar?.dismiss()
        errorSnackbar = null
        _binding = null
    }

    private fun updateUI(state: FragmentState) {
        binding.apply {
            loanCardDetail.visibility = state.uiVisibility
            progressBar.visibility = state.progressVisibility
        }
    }

    private fun showError(msg: String, actionName: String, action: (View) -> Unit) {
        errorSnackbar = Snackbar.make(
            binding.loanCardDetail,
            msg,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionName, action)
        errorSnackbar?.show()
    }
}