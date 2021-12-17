package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanRegistrationBinding
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.EditTextError
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

            firstNameEt.error = getString(R.string.field_should_be_not_empty)
            lastNameEt.error = getString(R.string.field_should_be_not_empty)
            phoneNumberEt.error = getString(R.string.field_should_be_not_empty)
            amountEt.error = getString(R.string.field_should_be_not_empty)

            percentTv.text = getString(R.string.percent_tv, viewModel.loanConditions.value!!.percent.toString())

            loanRegistrationBtn.setOnClickListener {
                    viewModel.registerLoan(getLoanRequest())
            }

            amountEt.doAfterTextChanged {
                viewModel.checkAmountIsValid(it?.toString())
            }
        }

        viewModel.apply {

            loanRegistrationState.observe(viewLifecycleOwner, { state ->
                when (state) {
                    LoanRegistrationFragmentState.UNKNOWN_HOST ->
                        showError(
                            getString(R.string.unknown_host_exception_msg),
                            getString(R.string.repeat)
                        ) {
                            viewModel.registerLoan(getLoanRequest())
                        }
                    LoanRegistrationFragmentState.TIMEOUT ->
                        showError(
                            getString(R.string.timeout_exception_msg),
                            getString(R.string.repeat)
                        ) {
                            viewModel.registerLoan(getLoanRequest())
                        }
                    LoanRegistrationFragmentState.LOADED ->
                        findNavController().navigate(R.id.next_action)
                    LoanRegistrationFragmentState.INCORRECT_INPUT_DATA ->
                        showError(
                            getString(R.string.incorrect_input_msg),
                            getString(R.string.ok)
                        ) {}
                }
            })

            editTextError.observe(viewLifecycleOwner, { editTextError ->
                when (editTextError) {
                    EditTextError.EXCEED_MAX_AMOUNT ->
                        binding.amountEt.error =
                            getString(R.string.max_amount_exceeded_msg, viewModel.loanConditions.value!!.maxAmount.toString())
                    EditTextError.AMOUNT_EMPTY ->
                        binding.amountEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.FIRST_NAME_EMPTY ->
                        binding.firstNameEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.LAST_NAME_EMPTY ->
                        binding.lastNameEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.NUMBER_EMPTY ->
                        binding.phoneNumberEt.error = getString(R.string.field_should_be_not_empty)
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
       mapOf(
           "amount" to binding.amountEt.text?.toString(),
            "firstName" to binding.firstNameEt.text?.toString(),
            "lastName" to binding.lastNameEt.text?.toString(),
            "phoneNumber" to binding.phoneNumberEt.text?.toString()
        )
}