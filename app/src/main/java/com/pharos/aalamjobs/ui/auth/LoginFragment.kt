package com.pharos.aalamjobs.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.pharos.aalamjobs.data.TokenObtainPair
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.databinding.FragmentLoginBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.enable
import com.pharos.aalamjobs.utils.handleApiError
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.progressbar.visible(false)
        binding.buttonLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.access/*, it.value.access*/)
                        //TODO write logic about if user was in other screen open that screen
                        requireActivity().startNewActivity(MainActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it) { login() }
            }
        })

        binding.etLogin.addTextChangedListener {
            val username = binding.etLogin.text.toString().trim()
            binding.buttonLogin.enable(username.isNotEmpty() && it.toString().isNotEmpty())

        }

        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = binding.etLogin.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val tokenObtainPair = TokenObtainPair(username, password)
        viewModel.login(tokenObtainPair)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java), userPreferences
    )
}