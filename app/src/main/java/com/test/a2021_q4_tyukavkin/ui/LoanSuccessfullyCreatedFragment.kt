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
import com.test.a2021_q4_tyukavkin.presentation.LoanRegistrationViewModel
import javax.inject.Inject

class LoanSuccessfullyCreatedFragment: Fragment() {

    private var _binding: FragmentLoanSuccessfullyCreatedBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanRegistrationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[LoanRegistrationViewModel::class.java]
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

        viewModel.loan.observe(this, {
            binding.loanDetailsTv.text = it.toString()
        })
    }
}