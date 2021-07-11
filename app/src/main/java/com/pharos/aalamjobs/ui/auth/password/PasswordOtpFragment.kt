package com.pharos.aalamjobs.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.databinding.FragmentPasswordOtpBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.register.RegisterFragment
import com.pharos.aalamjobs.ui.auth.register.SmsBroadcastReceiver
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.fragment_otp.*

class PasswordOtpFragment : BaseFragment<AuthViewModel, FragmentPasswordOtpBinding, AuthRepository>() {

    lateinit var auth: FirebaseAuth
    lateinit var reg: RegisterFragment
    private var inputText: String? = ""
    private var phone: String? = ""
    private var codeSent: String? = null

    var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private val REQ_USER_CONSENT = 200

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressbar.visible(false)

        inputText = arguments?.getString("storedVerificationId")
//        codeSent = arguments?.getString("storedVerificationId", "")
        phone = arguments?.getString("phone")

        binding.buttonValidate.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(
                inputText!!,
                binding.pinviewOtp.text.toString()
            )
//            signInWithCredentials(credential)
            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
               val bundle = Bundle()
              bundle.putString("phone", phone)
                    findNavController().navigate(R.id.action_passwordOtpFragment_to_newPasswordFragment, bundle)

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

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentPasswordOtpBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository (
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

}