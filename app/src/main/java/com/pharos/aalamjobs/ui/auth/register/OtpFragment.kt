package com.pharos.aalamjobs.ui.auth.register

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
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
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.fragment_otp.*
import okhttp3.ResponseBody
import java.util.regex.Pattern


class OtpFragment : BaseFragment<AuthViewModel, FragmentOtpBinding, AuthRepository>(),
    LoginListener {
    lateinit var auth: FirebaseAuth
    lateinit var reg: RegisterFragment
    private var inputText: String? = ""
    private var username: String? = ""
    private var password: String? = ""
    private var role: String? = ""
    private var fullname: String? = ""
    private var codeSent: String? = null

    var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private val REQ_USER_CONSENT = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startSmartUserConsent()
    }

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
//        codeSent = arguments?.getString("storedVerificationId", "")
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
//            signInWithCredentials(credential)
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.createNewUser(createUserModel)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
//                binding.prBarCodeConfirmRegister.hide()
//                binding.codeErrorRegister.visibility = View.VISIBLE
//                binding.codeErrorRegister.text = getString(R.string.wrongCode)
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressbar.visible(false)
        reg = RegisterFragment()

//        tv_resendotp.setOnClickListener {
//            reg.login()
//            progressbar.visible(true)
//        }
        tv_wrongnumber.setOnClickListener {
            findNavController().navigate(R.id.nav_register)
            progressbar.visible(true)
        }

        auth = FirebaseAuth.getInstance()

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
// Sign in failed, display a message and update the UI
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                    Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(requireActivity())
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            Toast.makeText(
                requireContext(),
                "On Success",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "On Failure",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getOtpFromMessage(message: String?) {
        val otpPattern = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            binding.pinviewOtp.setText(matcher.group(0))
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SmsBroadcastReceiverListener{
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {

                }

            }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }


    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOtpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

    override fun isUserExists(exists: Boolean) {
    }

    override fun signInFail(errorCode: ResponseBody?, code: Int?) {
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

    private fun startSmartUserConsent(){
        val client = SmsRetriever.getClient(requireContext())
        client.startSmsUserConsent(null)
    }


}