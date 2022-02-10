package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanSuccessfullyCreatedBinding
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

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().apply {
                repeat(3) {
                    popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}