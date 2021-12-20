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

        viewModel.apply {

            arguments?.let { viewModel.getLoanData(it.getLong("ID")) }

            loanPresentation.observe(viewLifecycleOwner, { loan ->
                binding.apply {
                    loanRequestNumber.text = getString(R.string.request_number, loan.id.toString())
                    loanRequestStatus.text = getString(R.string.loan_request_status, loan.state)
                    borrowerName.text = getString(R.string.borrower_name, loan.firstName, loan.lastName)
                    borrowerPhoneNumber.text = getString(R.string.borrower_phone_number, loan.phoneNumber)
                    loanAmount.text = getString(R.string.loan_amount, loan.amount.toString())
                    loanPercent.text = loan.percent
                    loanPeriod.text = getString(R.string.loan_period, loan.period.toString())
                    loanDate.text = getString(R.string.loan_date, loan.date, loan.time)
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

            isApproved.observe(viewLifecycleOwner, {
                binding.approvedInstructionsTv.visibility = View.VISIBLE
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