package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanRegistrationBinding
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanRegistrationViewModel
import javax.inject.Inject

class LoanRegistrationFragment : Fragment() {

    private var _binding: FragmentLoanRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            )[LoanRegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            percentTv.text = "${viewModel.loanConditions.value!!.percent}%"

            loanRegistrationBtn.setOnClickListener {
                Log.i("MyTAG", "Clicked register")
                viewModel.registerLoan(getLoanRequest())
            }

        }

        viewModel.apply {
            loan.observe(viewLifecycleOwner, { loan ->
                loan?.let {
                    findNavController().apply {
                        navigate(R.id.next_action)
                    }
                }
            })

            loanRegistrationState.observe(viewLifecycleOwner, { state ->
                when (state) {
                    FragmentState.UNKNOWN_HOST ->
                        showError(
                            getString(R.string.unknown_host_exception_msg),
                            getString(R.string.repeat)
                        ) {
                            viewModel.registerLoan(getLoanRequest())
                        }
                    FragmentState.TIMEOUT ->
                        showError(
                            getString(R.string.timeout_exception_msg),
                            getString(R.string.repeat)
                        ) {
                            viewModel.registerLoan(getLoanRequest())
                        }
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

    private fun showError(msg: String, actionName: String, action: (View) -> Unit) {
        errorSnackbar = Snackbar.make(
            binding.loanCardConditions,
            msg,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionName, action)
        errorSnackbar?.show()
    }

    private fun getLoanRequest() =
        LoanRequest(
            amount = binding.amountEt.text.toString().toLong(), //TODO Ограничить max value
            firstName = binding.firstNameEt.text.toString(),
            lastName = binding.lastNameEt.text.toString(),
            percent = viewModel.loanConditions.value!!.percent, //TODO Null safety
            period = viewModel.loanConditions.value!!.period, //TODO Null safety
            phoneNumber = binding.phoneNumberEt.text.toString()
        )

}