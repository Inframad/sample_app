package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoansHistoryBinding
import com.test.a2021_q4_tyukavkin.presentation.LoanHistoryFragmentViewModel
import com.test.a2021_q4_tyukavkin.presentation.LoanHistoryState
import javax.inject.Inject

class LoansHistoryFragment : Fragment() {

    private var _binding: FragmentLoansHistoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanHistoryFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[LoanHistoryFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoansHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            state.observe(this@LoansHistoryFragment, { state ->
                updateUI(state)
            })

            loans.observe(this@LoansHistoryFragment, { loans ->
                binding.loansRv.apply {
                    val bundle = Bundle()
                    val loanAdapter = LoanAdapter { id ->
                        bundle.putLong("ID", id)
                        findNavController().navigate(
                            R.id.next_action, bundle
                        )
                    }
                    loanAdapter.loans = loans
                    adapter = loanAdapter
                }
            })

        }

    }

    private fun updateUI(state: LoanHistoryState) {
        binding.progressBar.visibility = state.progressVisibility
        binding.loansRv.visibility = state.rvVisibility
    }
}

