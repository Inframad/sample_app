package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.databinding.FragmentRegistrationBinding
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.presentation.RegistrationFragmentViewModel
import javax.inject.Inject

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var registrationFragmentViewModel: RegistrationFragmentViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent.inject(this)
        registrationFragmentViewModel =
            ViewModelProvider(this, viewModelFactory)[RegistrationFragmentViewModel::class.java]
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

        binding.apply {

            registerBtn.setOnClickListener {
                registrationFragmentViewModel.register(
                    Auth(
                        name = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )
            }

            loginBtn.setOnClickListener {
                registrationFragmentViewModel.login(
                    Auth(
                        name = binding.loginEt.text.toString(),
                        password = binding.passwordEt.text.toString()
                    )
                )
            }
        }

        registrationFragmentViewModel.apply {
            response.observe(this@RegistrationFragment, {
                Toast.makeText(requireContext(), "${it.name} \n ${it.role.toString()}", Toast.LENGTH_SHORT).show()
                Log.i("ServerResponse", "Response observer")
            })

            token.observe(this@RegistrationFragment, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })
        }
    }
}