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
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoansHistoryBinding
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanHistoryFragmentViewModel
import javax.inject.Inject

class LoansHistoryFragment : Fragment() {

    private var _binding: FragmentLoansHistoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanHistoryFragmentViewModel

    private var errorSnackbar: Snackbar? = null
    private var loanAdapter = LoanListAdapter { id ->
        val bundle = Bundle()
        bundle.putLong("ID", id)
        findNavController().navigate(
            R.id.next_action, bundle
        )
    }

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

        binding.loansRv.adapter = loanAdapter

        viewModel.apply {

            getLoans()

            state.observe(viewLifecycleOwner, { state ->
                when (state) {
                    FragmentState.UNKNOWN_HOST ->
                        showError(
                            "Проблемы с интернет соединением",
                            "Обновить"
                        ) {
                            getLoans()
                        }
                    FragmentState.TIMEOUT ->
                        showError(
                            "Время ожидания ответа сервера истекло",
                            "Обновить"
                        ) {
                            getLoans()
                        }
                    else -> updateUI(state)
                }
            })

            loans.observe(viewLifecycleOwner, { loans ->
                binding.loansRv.apply {
                    loanAdapter.submitList(loans)
                }
            })

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        errorSnackbar?.dismiss()
    }

    private fun updateUI(state: FragmentState) {
        binding.progressBar.visibility = state.progressVisibility
        binding.loansRv.visibility = state.uiVisibility
    }

    private fun showError(msg: String, actionName: String, action: (View) -> Unit) {
        errorSnackbar = Snackbar.make(
            binding.loansRv,
            msg,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionName, action)
        errorSnackbar?.show()
    }
}

