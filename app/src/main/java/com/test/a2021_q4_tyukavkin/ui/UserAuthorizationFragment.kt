package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentRegistrationBinding
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.UserAuthorizationFragmentViewModel
import javax.inject.Inject

class UserAuthorizationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserAuthorizationFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(
                this@UserAuthorizationFragment,
                viewModelFactory
            )[UserAuthorizationFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNetworkConnectionListener()

        binding.apply {

            registerBtn.setOnClickListener {
                viewModel.register(
                    Auth(
                        name = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )
            }

            loginBtn.setOnClickListener {
                Log.i("MyTAG", "Login pressed")
                viewModel.login(
                    Auth(
                        name = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )

            }
        }

        viewModel.apply {
            user.observe(viewLifecycleOwner, { user ->
                Toast.makeText(
                    requireContext(),
                    "Вы успешно зарегистрированы, ${user.name}}",
                    Toast.LENGTH_SHORT
                ).show()
            })

            state.observe(viewLifecycleOwner, { state ->
                when (state) {
                    UserAuthorizationFragmentState.LOADED -> {
                        Log.i("State", "LOADED")
                        this@UserAuthorizationFragment.findNavController().apply {
                            navigate(R.id.next_action)
                        }
                    }
                    else -> updateUI(state)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI(state: UserAuthorizationFragmentState) {
        binding.apply {
            loginBtn.isEnabled = state.buttonsIsEnabled
            registerBtn.isEnabled = state.buttonsIsEnabled
            progressBar.visibility = state.progressVisibility
            warningMessageTv.text = state.warningMsg
        }
    }

    private fun setNetworkConnectionListener() {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                viewModel.setNetworkState(isAvailable = true)
            }

            override fun onLost(network: Network) {
                viewModel.setNetworkState(isAvailable = false)
            }
        }

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

}