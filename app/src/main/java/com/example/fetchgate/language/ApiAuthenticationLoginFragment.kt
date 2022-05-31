package com.example.fetchgate.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.fetchgate.MainActivity
import com.example.fetchgate.databinding.FragmentApiAuthenticationLoginBinding
import com.example.fetchgate.language.base.BaseFragment
import com.example.fetchgate.network.ApiService
import com.example.fetchgate.repository.AuthRepository
import com.example.fetchgate.utils.*
import kotlinx.coroutines.launch


class ApiAuthenticationLoginFragment :
    BaseFragment<AuthViewModel, FragmentApiAuthenticationLoginBinding, AuthRepository>() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressBar.visible(false)
        binding.login.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner) {
            binding.progressBar.visible(it is ResourceAuth.Loading)
            when (it) {
                is ResourceAuth.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.user.access_token!!)
                        requireActivity().startNewActivity(MainActivity::class.java)
                    }

                }
                is ResourceAuth.Failure -> handleApiError(it)
                else -> {}
            }
        }

        binding.edtPassword.addTextChangedListener {
            val email = binding.edtEmail.text.toString().trim()
            binding.login.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        with(binding) {
            login.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                binding.progressBar.visible(true)
                viewModel.login(email, password)
            }
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentApiAuthenticationLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(ApiService::class.java), userPreferences)
}