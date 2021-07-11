package com.pharos.aalamjobs.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.CreateUserModel
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.databinding.FragmentRegisterBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.LoginListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.handleApiError
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit


class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>(),
    LoginListener {

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressbar.visible(false)



        auth = FirebaseAuth.getInstance()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.progressbar.visible(it is Resource.Loading)

            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
//                        viewModel.saveUser(it.value.username, it.value.id, it.value.email)
                    }
                }
                is Resource.Failure -> handleApiError(it) { }
            }
        })


        binding.buttonLogin.setOnClickListener {
            checkPhone()
            initUI()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }

    }

    private fun checkPhone() {
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etPhonenumber.text.toString().trim()
        viewModel.checkPhone(username)
    }

    private fun signInWithPhone(authCredential: PhoneAuthCredential) {
        val password = binding.etPassword.text.toString().trim()
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etPhonenumber.text.toString().trim()
        val role = binding.spinnerRole.selectedItem.toString().trim()
        val fullname = binding.etFullName.text.toString().trim()
       val createUserModel = CreateUserModel(username, password, role, fullname)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful)
                viewModel.createNewUser(createUserModel)
            else
                Log.d("RegisterRegularFragment", "signInwithPhone: failed ${it.exception}")
        }
    }

    private fun initCallbackClient() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhone(credential)
//                Toast.makeText(requireContext(), "Success + $credential", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressbar.visible(false)
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)

                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val password = binding.etPassword.text.toString().trim()
                val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etPhonenumber.text.toString().trim()
                val role = binding.spinnerRole.selectedItem.toString().trim()
                val fullname = binding.etFullName.text.toString().trim()


                val bundle = Bundle()
                bundle.putString("storedVerificationId", storedVerificationId)
                bundle.putString("username", username)
                bundle.putString("password", password)
                bundle.putString("role", role)
                bundle.putString("fullname", fullname)

                findNavController().navigate(R.id.action_nav_register_to_nav_otp, bundle)
            }
        }
    }


    private fun initUI() {

        initCallbackClient()
        getOtp()

    }


    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun getOtp() {
        progressbar.visible(true)
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etPhonenumber.text.toString().trim()
        if (username.isNotEmpty()) {
            sendVerificationCode(username)
        } else {
            Toast.makeText(requireContext(), "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

    override fun isUserExists(available: Boolean) {
        if (!available) {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()

        } else {
            val username = "+" + binding.etPhoneCode.selectedCountryCode.toString().trim() + binding.etPhonenumber.text.toString().trim()

            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(username)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override fun signInFail(errorCode: ResponseBody?, code: Int?) {
        TODO("Not yet implemented")
    }

    override fun userDataSavedLogin() {
        TODO("Not yet implemented")
    }

    override fun loginSuccess(loginResponse: LoginResponse) {
        TODO("Not yet implemented")
    }

    override fun loginFail(code: Int?) {
        TODO("Not yet implemented")
    }

}













