package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanConditionsBinding
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanRegistrationViewModel
import javax.inject.Inject

class LoanConditionsFragment: Fragment() {

    private var _binding: FragmentLoanConditionsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

    private var errorSnackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[LoanRegistrationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            loanConditions.observe(viewLifecycleOwner, { loanConditions ->

                binding.apply {
                    loanRequestMaxAmount.text = loanConditions.maxAmount.toString()
                    loanRequestPercent.append(" ${loanConditions.percent}")
                    loanRequestPeriod.append(" ${loanConditions.period}")
                }

            })

            conditionsState.observe(viewLifecycleOwner, { state ->
                when (state) {
                    FragmentState.UNKNOWN_HOST ->
                        showError(
                            "Проблемы с интернет соединением",
                            "Обновить"
                        ) {
                            viewModel.getLoanConditions()
                        }
                    FragmentState.TIMEOUT ->
                        showError(
                            "Время ожидания ответа сервера истекло",
                            "Обновить"
                        ) {
                            viewModel.getLoanConditions()
                        }
                    else -> updateUI(state)
                }
            })
        }

        binding.registerLoanBtn.setOnClickListener {
            findNavController().navigate(R.id.next_action)
        }
    }

    private fun updateUI(state: FragmentState) {
        binding.apply {
            loanConditionsCard.visibility = state.uiVisibility
            progressBar.visibility = state.progressVisibility
        }
    }

    private fun showError(msg: String, actionName: String, action: (View) -> Unit) {
        errorSnackbar = Snackbar.make(
            binding.loanConditionsCard,
            msg,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionName, action)
        errorSnackbar?.show()
    }

}