package com.pharos.aalamjobs.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.ForgotPasswordModel
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.databinding.FragmentNewPasswordBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible


class NewPasswordFragment :
    BaseFragment<AuthViewModel, FragmentNewPasswordBinding, AuthRepository>(),
    ResetPasswordListener {
    private var phone: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone = arguments?.getString("phone")

        binding.btnSave.setOnClickListener {
            val newPwd = binding.etNewPassword.text.toString().trim()
            val confirmPwd = binding.etConfirmNewPassword.text.toString().trim()
            val forgotPasswordModel = ForgotPasswordModel(phone!!, newPwd)

            if (newPwd == confirmPwd) {
                viewModel.forgotPassword(forgotPasswordModel)
                binding.progressbar.visible(true)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.pwd_changed_success),
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().startNewActivity(AuthActivity::class.java)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.pwd_mismatch),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewPasswordBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(
        remoteDataSource.buildApiWithoutToken(AuthApi::class.java), userPreferences
    )

    override fun passwordChangedSuccess() {
    }

    override fun resetPasswordFail() {
    }

}