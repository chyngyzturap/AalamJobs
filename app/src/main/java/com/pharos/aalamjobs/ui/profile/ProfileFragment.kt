package com.pharos.aalamjobs.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentProfileBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvActivity
import com.pharos.aalamjobs.ui.settings.SettingsFragment
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.hideSoftKeyboard
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileFragment : BaseFragment<AuthViewModel, FragmentProfileBinding, AuthRepository>(),
    SettingsFragment.QuitListener, UserListener{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener {
            hideSoftKeyboard(requireActivity())
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().nav_bottom.visible(true)
        showCurrent()

        binding.btnMyCv.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()){
                showSignUpDialog()
            } else {
                val intent = Intent(requireContext(), CvActivity::class.java)
                startActivity(intent)
            }
        }

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_settingsFragment)
        }

        binding.ivSettingsNoToken.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_settingsFragment)
        }

        binding.ivEdit.setOnClickListener {
            recreate(requireActivity())
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()){
                showSignUpDialog()
            } else {
                findNavController().navigate(R.id.action_nav_profile_to_editProfileFragment)
            }
        }

        binding.ivEditNoToken.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()){
                showSignUpDialog()
            } else {
                findNavController().navigate(R.id.action_nav_profile_to_editProfileFragment)
            }
        }

        binding.btnLogIn.setOnClickListener {
            val intent = Intent (requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSignUpDialog() {
        val signUpDialogFragment = SignUpDialogFragment()
        val manager = requireActivity().supportFragmentManager
        signUpDialogFragment.show(manager, "signUpDialog")
    }

    private fun showCurrent() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
        } else {
            binding.profileTokenContainer.visible(true)
            binding.profileNoTokenContainer.visible(false)
            viewModel.setUserListener(this)
            viewModel.getProfileInfo()
        }
    }

    override fun onQuitConfirmed() {
        viewModel.logout()
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(AuthApi::class.java, token)
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)

        if (token.isNullOrEmpty()){
            return AuthRepository(apiNoToken, userPreferences)
        } else {
            return AuthRepository(api, userPreferences)
        }
    }

    override fun setUserData(userResponse: UserResponse) {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
            showSignUpDialog()
        } else {
            initUserData(userResponse)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUserData(userResponse: UserResponse){
        if(userResponse.city != null && userResponse.country != null
            && userResponse.city != "" && userResponse.country != ""){
            binding.tvLocation.text = userResponse.city + ", " + userResponse.country
        } else {
            binding.tvLocation.visible(false)
        }
        if (userResponse.position != "" && userResponse.position != null){
            binding.profileTvPosition.text = userResponse.position
        } else {
            binding.profileIvJobTitle.visible(false)
            binding.profileTitleContainer.visible(false)
        }
        if (userResponse.username != "") {
            binding.profileTvPhone.text = userResponse.username
        } else {
            binding.profilePhoneContainer.visible(false)
        }
        if (userResponse.email != ""){
            binding.profileTvEmail.text = userResponse.email
        } else {
            binding.profileEmailContainer.visible(false)
        }
        if (userResponse.fullname != ""){
            binding.tvFullName.text = userResponse.fullname
        } else {
            binding.tvFullName.visible(false)
        }

        lifecycleScope.launch {
            viewModel.saveUser(userResponse.email, userResponse.id, userResponse.username,
            userResponse.photo, userResponse.city, userResponse.country, userResponse.position,
            userResponse.fullname)
        }

        if (userResponse.photo != "") {
            Glide.with(binding.root).load(userResponse.photo)
                .into(binding.ivProfilePhoto)
        }
    }

    override fun dataError(code: Int?) {
    }
}