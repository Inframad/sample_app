package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanDetailsBinding
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanDetailsFragmentViewModel
import com.test.a2021_q4_tyukavkin.presentation.state.LoanDetailsState
import com.test.a2021_q4_tyukavkin.presentation.formatOffsetDateTimeToString
import javax.inject.Inject

class LoanDetailsFragment : Fragment() {

    private var _binding: FragmentLoanDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanDetailsFragmentViewModel

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

        arguments?.let { viewModel.getLoanData(it.getLong("ID")) }

        viewModel.apply {

            loan.observe(viewLifecycleOwner, { loan ->
                binding.apply { //TODO Смена ориентации
                    loanRequestNumber.append(loan.id.toString())
                    loanRequestStatus.append(loan.state.toString())
                    borrowerFirstName.append(loan.firstName)
                    borrowerLastName.append(loan.lastName)
                    borrowerPhoneNumber.append(loan.phoneNumber)
                    loanAmount.append(loan.amount.toString())
                    loanPercent.text = loan.percent.toString()
                    loanPercent.append("%")
                    loanPeriod.append(loan.period.toString())
                    loanRequestDate.append("${loan.date} ${loan.time}")
                    //TODO Отображение даты
                }
            })

            state.observe(viewLifecycleOwner, { state ->
                updateUI(state)
            })
        }
    }

    private fun updateUI(state: LoanDetailsState) {
        binding.apply {
            loanCardDetail.visibility = state.loanCardDetailVisibility
            progressBar.visibility = state.progressVisibility
        }
    }
}