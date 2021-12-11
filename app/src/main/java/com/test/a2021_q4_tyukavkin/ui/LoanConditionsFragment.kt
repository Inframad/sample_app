package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanConditionsBinding
import com.test.a2021_q4_tyukavkin.presentation.state.LoanConditionsFragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanRegistrationViewModel
import javax.inject.Inject

class LoanConditionsFragment: Fragment() {

    private var _binding: FragmentLoanConditionsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

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
                updateUI(state)
            })
        }

        binding.registerLoanBtn.setOnClickListener {
            findNavController().navigate(R.id.next_action)
        }
    }

    private fun updateUI(state: LoanConditionsFragmentState) {
        binding.apply {
            loanConditionsCard.visibility = state.loanConditionsCardVisibility
            progressBar.visibility = state.progressBarVisibility
        }
    }

}