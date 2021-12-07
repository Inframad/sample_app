package com.test.a2021_q4_tyukavkin.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoanConditionsBinding
import com.test.a2021_q4_tyukavkin.presentation.LoanRegistrationViewModel
import javax.inject.Inject

class LoanConditionsFragment @Inject constructor(
) : Fragment() {

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoanConditions()

        viewModel.loanConditions.observe(this, {
            binding.loanConditionsTv.text =
                "Максимальная сумма: ${it.maxAmount} \n" +
                        "Процентная ставка: ${it.percent} \n" +
                        "Период: ${it.period}"
        })

        binding.registerLoanBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoanRegistrationFragment())
                .commit()
        }
    }

}