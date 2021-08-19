package com.pharos.aalamjobs.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.CreateUserModel
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.databinding.FragmentRegisterBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.LoginListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.hideSoftKeyboard
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>(),
    LoginListener {

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            hideSoftKeyboard(requireActivity())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressbar.visible(false)
        viewModel.setLoginListener(this)
        auth = FirebaseAuth.getInstance()
        setupListeners()

        binding.buttonLogin.setOnClickListener {
            checkPhone()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }
    }

    private fun checkPhone() {
        binding.progressbar.visible(true)
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString()
            .trim() + binding.etPhonenumber.text.toString().trim()
        viewModel.checkPhone(username)
    }

    private fun signInWithPhone(authCredential: PhoneAuthCredential) {
        val password = binding.etPasswordRegister.text.toString().trim()
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString()
            .trim() + binding.etPhonenumber.text.toString().trim()
        val role = binding.spinnerRole.selectedItem.toString().trim()
        val fullname = binding.etFullName.text.toString().trim()
        val createUserModel = CreateUserModel(username, password, role, fullname)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful)
                Log.d("RegisterRegularFragment", "signInwithPhone: success ${it.result}")
            else
                Log.d("RegisterRegularFragment", "signInwithPhone: failed ${it.exception}")
        }
    }

    private fun initCallbackClient() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhone(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressbar.visible(false)
                Toast.makeText(requireContext(), "Failed + $e", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val password = binding.etPasswordRegister.text.toString().trim()
                val username = "+" + binding.etPhoneCode.selectedCountryCode.toString()
                    .trim() + binding.etPhonenumber.text.toString().trim()
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
        val username = "+" + binding.etPhoneCode.selectedCountryCode.toString()
            .trim() + binding.etPhonenumber.text.toString().trim()
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
        if (available) {
            initUI()
        } else {
            binding.progressbar.visible(false)
            Toast.makeText(requireContext(), getString(R.string.exist_user), Toast.LENGTH_SHORT)
                .show()
        }
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            when (view.id) {
                R.id.et_password_register -> {
                    validatePassword()
                }
            }
        }
    }

    private fun validatePassword(): Boolean {
        if (binding.etPasswordRegister.text.toString().length < 8) {
            binding.tvPassword.error = getString(R.string.password_length)
            binding.tvPassword.refreshErrorIconDrawableState()
            binding.etPasswordRegister.requestFocus()
            return false
        } else {
            binding.tvPassword.error = null
        }
        if (!isStringContainNumber(binding.etPasswordRegister.text.toString())) {
            binding.tvPassword.error = getString(R.string.password_numeric)
            binding.tvPassword.refreshErrorIconDrawableState()
            binding.etPasswordRegister.requestFocus()
            return false
        } else {
            binding.tvPassword.error = null
        }
        return true
    }

    private fun isStringContainNumber(text: String): Boolean {
        val pattern = Pattern.compile(".*[a-z].*")
        val matcher = pattern.matcher(text)
        return matcher.matches()
    }

    private fun setupListeners() {
        binding.etPasswordRegister.addTextChangedListener(TextFieldValidation(binding.etPasswordRegister))
    }

    override fun signInFail(errorCode: ResponseBody?, code: Int?) {
    }

    override fun userDataSavedLogin() {
    }

    override fun loginSuccess(loginResponse: LoginResponse) {
    }

    override fun loginFail(code: Int?) {
    }

}














