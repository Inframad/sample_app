package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.FragmentRegistrationBinding
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.exception.LoginError
import com.test.a2021_q4_tyukavkin.domain.entity.exception.PasswordError
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState.*
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.UserAuthorizationFragmentViewModel
import javax.inject.Inject

class UserAuthorizationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserAuthorizationFragmentViewModel

    private var connectivityManager: ConnectivityManager? = null

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            viewModel.setNetworkState(isAvailable = true)
        }

        override fun onLost(network: Network) {
            viewModel.setNetworkState(isAvailable = false)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireContext().applicationContext as App).appComponent.inject(this)
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

        setHasOptionsMenu(true)

        setNetworkConnectionListener()

        binding.apply {

            registerBtn.setOnClickListener {
                viewModel.register(
                    Auth(
                        username = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )
            }

            loginBtn.setOnClickListener {
                Log.i("MyTAG", "Login pressed")
                viewModel.login(
                    Auth(
                        username = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )

            }
        }

        viewModel.apply {
            user.observe(viewLifecycleOwner, { user ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.successful_registration, user.name),
                    Toast.LENGTH_SHORT
                ).show()
            })

            state.observe(viewLifecycleOwner, { state ->
                when (state) {
                    LOADED ->
                        this@UserAuthorizationFragment.findNavController().apply {
                            navigate(R.id.next_action)
                        }
                    NO_INTERNET_CONNECTION -> setWarningMessage(getString(R.string.check_network_connection_msg))
                    INVALID_CREDENTIALS -> setWarningMessage(getString(R.string.invalid_credentials_msg))
                    TIMEOUT_EXCEPTION -> setWarningMessage(getString(R.string.timeout_exception_msg))
                    BUSY_LOGIN -> setWarningMessage(getString(R.string.busy_login_msg))
                    SERVER_ERROR -> setWarningMessage(getString(R.string.server_unavailable))
                    DEFAULT -> updateUI(state)
                    LOADING -> updateUI(state)
                    else -> setWarningMessage(getString(R.string.unknown_error_msg))
                }
                updateUI(state)
            })

            loginError.observe(viewLifecycleOwner) { loginErrors ->
                showLoginErrors(loginErrors)
            }

            passwordError.observe(viewLifecycleOwner) { passwordErrors ->
                showPasswordErrors(passwordErrors)
            }
        }
    }

    private fun showPasswordErrors(passwordErrors: List<PasswordError>) {
        val stringBuilder = StringBuilder()

        val iterator = passwordErrors.iterator()
        iterator.forEach {

            when (it) {
                PasswordError.INCORRECT ->
                    stringBuilder.append(getString(R.string.incorre????_password_msg))
                PasswordError.TOO_SHORT ->
                    stringBuilder.append(getString(R.string.too_short_password_msg))
                PasswordError.TOO_COMMON ->
                    stringBuilder.append(getString(R.string.too_common_password_msg))
                PasswordError.ENTIRELY_NUMERIC ->
                    stringBuilder.append(getString(R.string.entirely_numeric_password_msg))
                PasswordError.BLANK_FIELD ->
                    stringBuilder.append(getString(R.string.empty_password_msg))
                PasswordError.TOO_SIMILAR_TO_LOGIN ->
                    stringBuilder.append(getString(R.string.password_too_similar_to_login_msg))
            }

            if (iterator.hasNext()) {
                stringBuilder.append(System.getProperty("line.separator"))
            }
        }

        binding.passwordEt.error = stringBuilder
    }

    private fun showLoginErrors(loginErrors: List<LoginError>) {
        val stringBuilder = StringBuilder()
        val iterator = loginErrors.iterator()

        iterator.forEach {
            when (it) {
                LoginError.BLANK_FIELD ->
                    stringBuilder.append(getString(R.string.login_empty_msg))
                LoginError.INCORRECT ->
                    stringBuilder.append(getString(R.string.incorrect_login_msg))
                LoginError.BUSY ->
                    stringBuilder.append(getString(R.string.busy_login_msg))
            }
        }

        if (iterator.hasNext()) {
            stringBuilder.append(System.getProperty("line.separator"))
        }

        binding.loginEt.error = stringBuilder
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        connectivityManager = null
    }

    private fun updateUI(state: UserAuthorizationFragmentState) {
        binding.apply {
            loginBtn.isEnabled = state.buttonsIsEnabled
            registerBtn.isEnabled = state.buttonsIsEnabled
            progressBar.visibility = state.progressVisibility
        }
    }

    private fun setNetworkConnectionListener() {
        connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager?.registerNetworkCallback(request, networkCallback)
        }
    }

    private fun setWarningMessage(msg: String) {
        binding.warningMessageTv.text = msg
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}