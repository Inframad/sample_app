package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanRegistrationBinding
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRegistrationInputData
import com.test.a2021_q4_tyukavkin.presentation.model.EditTextError
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanRegistrationViewModel
import javax.inject.Inject

class LoanRegistrationFragment : Fragment() {

    private var _binding: FragmentLoanRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

    private var errorSnackbar: Snackbar? = null

    private var ignoreNextTextChange = false
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

        if (savedInstanceState != null) ignoreNextTextChange = true

        binding.apply {

            initInputDataValidation()

            percentTv.text =
                getString(R.string.percent_tv, viewModel.loanConditions.value!!.percent.toString())

            loanRegistrationBtn.setOnClickListener {
                viewModel.registerLoan(getInputData())
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
                            viewModel.registerLoan(getInputData())
                        }
                    LoanRegistrationFragmentState.TIMEOUT ->
                        showError(
                            getString(R.string.timeout_exception_msg),
                            getString(R.string.repeat)
                        ) {
                            viewModel.registerLoan(getInputData())
                        }
                    LoanRegistrationFragmentState.LOADED ->
                        findNavController().navigate(R.id.next_action)
                    LoanRegistrationFragmentState.INCORRECT_INPUT_DATA ->
                        showError(
                            getString(R.string.incorrect_input_msg),
                            getString(R.string.ok)
                        ) {}
                    LoanRegistrationFragmentState.UNKNOWN_ERROR ->
                    showError(
                        getString(R.string.unknown_error_msg),
                        getString(R.string.ok)
                    ) {}
                    else -> {}
                }
            })

            editTextError.observe(viewLifecycleOwner, { editTextError ->
                when (editTextError) {
                    EditTextError.EXCEED_MAX_AMOUNT ->
                        binding.amountEt.error =
                            getString(
                                R.string.max_amount_exceeded_msg,
                                viewModel.loanConditions.value!!.maxAmount.toString()
                            )
                    EditTextError.AMOUNT_EMPTY ->
                        binding.amountEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.FIRST_NAME_EMPTY ->
                        binding.firstNameEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.LAST_NAME_EMPTY ->
                        binding.lastNameEt.error = getString(R.string.field_should_be_not_empty)
                    EditTextError.NUMBER_EMPTY ->
                        binding.phoneNumberEt.error = getString(R.string.field_should_be_not_empty)
                    else -> {}
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

    private fun getInputData(): LoanRegistrationInputData {
        binding.apply {
            return LoanRegistrationInputData(
                firstName = firstNameEt.text?.toString(),
                lastName = lastNameEt.text?.toString(),
                phoneNumber = phoneNumberEt.text?.toString(),
                amount = amountEt.text?.toString()
            )
        }
    }

    private fun initInputDataValidation() {

        if(!ignoreNextTextChange) viewModel.checkInputData(getInputData())

        binding.apply {

            amountEt.checkInputAfterTextChanged()
            firstNameEt.checkInputAfterTextChanged()
            lastNameEt.checkInputAfterTextChanged()
            phoneNumberEt.doAfterTextChanged {
                if(ignoreNextTextChange) {
                    ignoreNextTextChange = false
                    return@doAfterTextChanged
                }
                viewModel.checkInputData(getInputData())
            }
        }
    }

    private fun EditText.checkInputAfterTextChanged() {
        doAfterTextChanged {
            if(ignoreNextTextChange) return@doAfterTextChanged
            viewModel.checkInputData(getInputData())
        }
    }
}