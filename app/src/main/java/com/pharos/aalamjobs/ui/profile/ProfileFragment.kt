package com.pharos.aalamjobs.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentProfileBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvActivity
import com.pharos.aalamjobs.ui.settings.SettingsFragment
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.SignUpDialogFragment
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileFragment : BaseFragment<AuthViewModel, FragmentProfileBinding, AuthRepository>(),
    SettingsFragment.QuitListener, UserListener{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().nav_bottom.visible(true)

        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()){

        } else {
            viewModel.setUserListener(this)
            viewModel.getProfileInfo()
        }



        //Go to CV create
        btn_my_cv.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }

            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                val intent = Intent(requireContext(), CvActivity::class.java)
                startActivity(intent)
            }
        }

        iv_settings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_settingsFragment)
        }

        binding.ivEdit.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }


            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                findNavController().navigate(R.id.action_nav_profile_to_editProfileFragment)
            }

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
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            initUserData(userResponse)
        }

    }

    private fun initUserData(userResponse: UserResponse){
        binding.tvLocation.text = userResponse.city + ", " + userResponse.country
        binding.profileTvPosition.text = userResponse.position
        binding.profileTvPhone.text = userResponse.username
        binding.profileTvEmail.text = userResponse.email
        binding.tvFullName.text = userResponse.fullname

        lifecycleScope.launch {
            viewModel.saveUser(userResponse.email, userResponse.id, userResponse.username,
            userResponse.photo, userResponse.city, userResponse.country, userResponse.position,
            userResponse.fullname)
        }

        if (userResponse.photo.isNotEmpty())
            Glide.with(binding.root).load(userResponse.photo)
                .error(
                    ContextCompat.getDrawable(
                        binding.root.context, R.drawable.logo
                    )
                ).into(binding.ivProfilePhoto)
    }



    override fun dataError(code: Int?) {

    }

    override fun setUserId(id: Int?, logo: String?) {

    }

    override fun updateUserSuccess() {

    }

    override fun quitDone() {
        requireActivity().startNewActivity(SplashActivity::class.java)
    }

    private fun authAlertDialog(){
        val mAlertDialog = AlertDialog.Builder(requireContext())
//            mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon

        mAlertDialog.setPositiveButtonIcon(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_done_24_green
            )
        )
        mAlertDialog.setNegativeButtonIcon(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_cancel_24_red
            )
        )
        mAlertDialog.setTitle("Please sign up to get access") //set alertdialog title
//        mAlertDialog.setMessage(R.string.txt_logout_before_remind) //set alertdialog message
        mAlertDialog.setPositiveButton("Sign Up ") { dialog, id ->
            ActivityCompat.finishAffinity(requireActivity())
            val intent = Intent(requireContext(), SplashActivity::class.java)
            startActivity(intent)
        }
        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
            null
        }
        mAlertDialog.show()
    }
}