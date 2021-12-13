package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentLoansHistoryBinding
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.LoanHistoryFragmentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoansHistoryFragment : Fragment() {

    private var _binding: FragmentLoansHistoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoanHistoryFragmentViewModel

    private var errorSnackbar: Snackbar? = null

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

        val loanAdapter = LoanListAdapter { id ->
            val bundle = Bundle()
            bundle.putLong("ID", id)
            findNavController().navigate(
                R.id.next_action, bundle
            )
        }

        binding.swiperefresh.setOnRefreshListener {
            lifecycle.coroutineScope.launch {
                viewModel.updateLoans()
            }

        }

        binding.list.adapter = loanAdapter

        viewModel.apply {

            updateLoans()

            state.observe(viewLifecycleOwner, { state ->
                when (state) {
                    FragmentState.UNKNOWN_HOST ->
                        showError(
                            getString(R.string.unknown_host_exception_msg),
                            getString(R.string.refresh)
                        ) {
                           updateLoans()
                        }
                    FragmentState.TIMEOUT ->
                        showError(
                            getString(R.string.timeout_exception_msg),
                            getString(R.string.refresh)
                        ) {
                            updateLoans()
                        }
                    FragmentState.LOADING -> binding.swiperefresh.isRefreshing = true
                    FragmentState.LOADED -> binding.swiperefresh.isRefreshing = false
                    else -> {}
                }
            })

            lifecycle.coroutineScope.launch {
                getLoans().collect {
                    loanAdapter.submitList(it)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        errorSnackbar?.dismiss()
        _binding = null
        errorSnackbar = null
    }

    private fun showError(msg: String, actionName: String, action: (View) -> Unit) {
        errorSnackbar = Snackbar.make(
            binding.list,
            msg,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(actionName, action)
        errorSnackbar?.show()
    }
}

