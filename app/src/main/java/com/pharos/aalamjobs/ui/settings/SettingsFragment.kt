package com.pharos.aalamjobs.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentSettingsBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.LogOutDialogFragment
import com.pharos.aalamjobs.utils.SignUpDialogFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SettingsFragment : BaseFragment<AuthViewModel, FragmentSettingsBinding, AuthRepository>(),
UserListener{

    private var quitListener: QuitListener? = null
    private var preference: UserPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val token = runBlocking { userPreferences.tokenAccess.first() }


        if (token.isNullOrEmpty()){

        } else {
            viewModel.setUserListener(this)
            viewModel.getProfileInfo()
        }



        tv_settings_language.setOnClickListener {
            showChangeLang()
        }
        tv_settings_about_us.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_about_us, Toast.LENGTH_SHORT).show()
        }
        tv_settings_change_password.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }


            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                findNavController().navigate(R.id.action_settingsFragment_to_changePasswordFragment)
            }

        }
        iv_backpressed.setOnClickListener { requireActivity().onBackPressed() }
        tv_settings_contact_us.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_contact_us, Toast.LENGTH_SHORT).show()
        }
        tv_settings_help.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_help, Toast.LENGTH_SHORT).show()
        }
        binding.tvSettingsLogout.setOnClickListener {
            val logOutDialogFragment = LogOutDialogFragment()
            val manager = requireActivity().supportFragmentManager
            logOutDialogFragment.show(manager, "logOutDialog")
        }
//            val mAlertDialog = AlertDialog.Builder(requireContext())
////            mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon
//
//            mAlertDialog.setPositiveButtonIcon(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_done_24_green
//                )
//            )
//            mAlertDialog.setNegativeButtonIcon(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_cancel_24_red
//                )
//            )
//            mAlertDialog.setTitle(R.string.txt_logout_before) //set alertdialog title
//            mAlertDialog.setMessage(R.string.txt_logout_before_remind) //set alertdialog message
//            mAlertDialog.setPositiveButton("Logout") { dialog, id ->
//                quitListener?.onQuitConfirmed()
//                lifecycleScope.launch {
//                    userPreferences.clear()
//                }
//                finishAffinity(requireActivity())
//                val intent = Intent(requireContext(), SplashScreenActivity::class.java)
//                startActivity(intent)
//            }
//            mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
//                null
//            }
//            mAlertDialog.show()
//        }

    }

    private fun initUserData(userResponse: UserResponse){

        binding.tvSettingsProfileFullName.text = userResponse.fullname

        if (userResponse.photo.isNotEmpty())
            Glide.with(binding.root).load(userResponse.photo)
                .error(
                    ContextCompat.getDrawable(
                        binding.root.context, R.drawable.logo
                    )
                ).into(binding.ivProfileSettings)
    }


    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(langList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    setLocale(requireContext(), "en")

                }
                1 -> {
                    setLocale(requireContext(), "ky")


                }
                2 -> {
                    setLocale(requireContext(), "ru")
                }
                3 -> {
                    setLocale(requireContext(), "tr")
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    @Suppress("DEPRECATION")
    private fun setLocale(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        lifecycleScope.launch {
            viewModel.saveLang(lang)
            Log.d("ololo", lang)
        }
        recreate(requireActivity())
    }

    interface QuitListener {
        fun onQuitConfirmed()
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentSettingsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() : AuthRepository {
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
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        } else {
            initUserData(userResponse)
        }

    }

    override fun dataError(code: Int?) {
    }

    override fun setUserId(id: Int?, logo: String?) {
    }

    override fun updateUserSuccess() {
    }

    override fun quitDone() {
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