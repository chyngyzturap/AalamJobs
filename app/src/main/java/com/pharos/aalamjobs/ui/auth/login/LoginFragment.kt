package com.pharos.aalamjobs.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.TokenObtainPair
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentLoginBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.handleApiError
import com.pharos.aalamjobs.utils.hideSoftKeyboard
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>(),
    UserListener {
    private var usernameIntent: String? = ""
    private var passwordIntent: String? = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fastLoginAfterReg()
        binding.progressbar.visible(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.access, it.value.refresh)
                        requireActivity().startNewActivity(MainActivity::class.java)
                    }
                }

                is Resource.Failure -> handleApiError(it) {
                    login()
                }
            }
        })

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }
        binding.buttonLogin.setOnClickListener {
            login()
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_forgotPasswordFragment)
        }

        binding.tvLoginWithEmail.setOnClickListener {
            binding.phoneContainer.visible(false)
            binding.tvEmail.visible(true)
            binding.tvLoginWithPhone.visible(true)
            binding.tvLoginWithEmail.visible(false)
        }

        binding.tvLoginWithPhone.setOnClickListener {
            binding.tvEmail.visible(false)
            binding.phoneContainer.visible(true)
            binding.tvLoginWithEmail.visible(true)
            binding.tvLoginWithPhone.visible(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            hideSoftKeyboard(requireActivity())
        }
    }

    private fun fastLoginAfterReg() {
        usernameIntent = arguments?.getString("username")
        passwordIntent = arguments?.getString("password")
        if (usernameIntent != "" && passwordIntent != ""
            && usernameIntent != null && passwordIntent != null
        ) {
            val tokenObtainPairPhone = TokenObtainPair(usernameIntent, passwordIntent)
            viewModel.login(tokenObtainPairPhone)
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val phoneNumber = binding.etLogin.text.toString().trim()
        val phone = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + phoneNumber
        val password = binding.etPassword.text.toString().trim()

        if (phoneNumber != "") {
            val tokenObtainPairPhone = TokenObtainPair(phone, password)
            viewModel.login(tokenObtainPairPhone)
        }
        if (email != "") {
            val tokenObtainPairEmail = TokenObtainPair(email, password)
            viewModel.login(tokenObtainPairEmail)
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApi(AuthApi::class.java), userPreferences
    )

    override fun setUserData(userResponse: UserResponse) {
    }

    override fun dataError(code: Int?) {
    }
}