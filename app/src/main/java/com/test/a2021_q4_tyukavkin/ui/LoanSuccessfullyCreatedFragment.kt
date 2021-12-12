package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanSuccessfullyCreatedBinding
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanRegistrationViewModel
import javax.inject.Inject

class LoanSuccessfullyCreatedFragment : Fragment() {

    private var _binding: FragmentLoanSuccessfullyCreatedBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanSuccessfullyCreatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            loan.observe(viewLifecycleOwner, { loan ->
                binding.apply { //TODO Смена ориентации
                    loanRequestNumber.append(loan.id.toString())
                    loanRequestStatus.append(loan.state)
                    borrowerFirstName.append(loan.firstName)
                    borrowerLastName.append(loan.lastName)
                    borrowerPhoneNumber.append(loan.phoneNumber)
                    loanAmount.append(loan.amount.toString())
                    loanPercent.text = loan.percent.toString()
                    loanPercent.append("%")
                    loanPeriod.append(loan.period.toString())
                    loanRequestDate.append(" ${loan.date} ${loan.time}")
                }
            })

            loanRegistrationState.observe(viewLifecycleOwner, { state ->
                updateUI(state)
            })
        }
    }

    private fun updateUI(state: FragmentState) {
        binding.apply {
            loanCardDetail.visibility = state.uiVisibility
            progressBar.visibility = state.progressVisibility
        }
    }
}