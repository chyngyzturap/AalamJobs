package com.pharos.aalamjobs.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.TokenObtainPair
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentLoginBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.handleApiError
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>(),
UserListener{


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.progressbar.visible(false)
//        binding.buttonLogin.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.access)
                        //TODO write logic about if user was in other screen open that screen
                        requireActivity().startNewActivity(MainActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it) { login() }
            }
        })

        binding.etLogin.addTextChangedListener {
            val username = binding.etLogin.text.toString().trim()
//            binding.buttonLogin.enable(username.isNotEmpty() && it.toString().isNotEmpty())

        }

        binding.tvSignup.setOnClickListener {

            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

        binding.buttonLogin.setOnClickListener {
            login()
        }

binding.tvForgotPassword.setOnClickListener {
    findNavController().navigate(R.id.action_nav_login_to_forgotPasswordFragment)
}
    }

    private fun login() {
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etLogin.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val tokenObtainPair = TokenObtainPair(username, password)
        viewModel.login(tokenObtainPair)
//        viewModel.login2(tokenObtainPair, 2)
//        requireActivity().startNewActivity(MainActivity::class.java)

    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java), userPreferences
    )


    private fun initUserData(loginResponse: LoginResponse?){
        val username = loginResponse?.username
        val token = loginResponse?.access

        if (username != null && token != null){
            requireActivity().startNewActivity(MainActivity::class.java)
        }
    }

    override fun setUserData(userResponse: UserResponse) {
        TODO("Not yet implemented")
    }

    override fun dataError(code: Int?) {
        TODO("Not yet implemented")
    }

    override fun setUserId(id: Int?, logo: String?) {
        TODO("Not yet implemented")
    }

    override fun updateUserSuccess() {
        TODO("Not yet implemented")
    }

    override fun quitDone() {
        TODO("Not yet implemented")
    }
}