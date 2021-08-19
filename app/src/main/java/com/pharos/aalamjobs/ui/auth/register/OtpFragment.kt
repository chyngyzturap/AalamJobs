package com.pharos.aalamjobs.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.CreateUserModel
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.databinding.FragmentOtpBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.LoginListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.hideSoftKeyboard
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.fragment_otp.*
import okhttp3.ResponseBody


class OtpFragment : BaseFragment<AuthViewModel, FragmentOtpBinding, AuthRepository>(),
    LoginListener, RegisterListener {
    lateinit var auth: FirebaseAuth
    lateinit var reg: RegisterFragment
    private var inputText: String? = ""
    private var username: String? = ""
    private var password: String? = ""
    private var role: String? = ""
    private var fullname: String? = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressbar.visible(false)

        binding.tvResendotp.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }

        binding.tvWrongnumber.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
        }

        inputText = arguments?.getString("storedVerificationId")
        username = arguments?.getString("username")
        password = arguments?.getString("password")
        role = arguments?.getString("role")
        fullname = arguments?.getString("fullname")
        val createUserModel = CreateUserModel(username, password, role, fullname)

        binding.buttonValidate.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(
                inputText!!,
                binding.pinviewOtp.text.toString()
            )
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.createNewUser(createUserModel)
                    binding.progressbar.visible(true)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            requireContext(),
                            "${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressbar.visible(false)
        viewModel.setRegisterListener(this)
        reg = RegisterFragment()

        binding.tvWrongnumber.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
            progressbar.visible(true)
        }

        auth = FirebaseAuth.getInstance()

        view.setOnClickListener {
            hideSoftKeyboard(requireActivity())
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOtpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

    override fun isUserExists(available: Boolean) {
    }

    override fun signInFail(errorCode: ResponseBody?, code: Int?) {
    }

    override fun userDataSavedLogin() {
    }

    override fun loginSuccess(loginResponse: LoginResponse) {
    }

    override fun loginFail(code: Int?) {
    }

    override fun createUserSuccess(username: String) {
        binding.progressbar.visible(false)
        password = arguments?.getString("password")
        val bundle = Bundle()
        bundle.putString("username", username)
        bundle.putString("password", password)
        findNavController().navigate(R.id.nav_login, bundle)
    }

    override fun createUserFailed(code: Int?) {
        Toast.makeText(requireContext(), "Failed $code", Toast.LENGTH_SHORT).show()
    }
}