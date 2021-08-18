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
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentSettingsBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.dialogfragments.LogOutDialogDialogFragment
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SettingsFragment : BaseFragment<AuthViewModel, FragmentSettingsBinding, AuthRepository>(),
    UserListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCurrent()

        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
        } else {
            viewModel.setUserListener(this)
            viewModel.getProfileInfo()
        }
        binding.tvSettingsLanguage.setOnClickListener {
            showChangeLang()
        }
        binding.tvSettingsAboutUs.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_about_us, Toast.LENGTH_SHORT).show()
        }
        binding.tvSettingsChangePassword.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()) {
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                findNavController().navigate(R.id.action_settingsFragment_to_changePasswordFragment)
            }
        }
        binding.ivBackpressed.setOnClickListener { requireActivity().onBackPressed() }
        binding.tvSettingsContactUs.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_contact_us, Toast.LENGTH_SHORT).show()
        }
        binding.tvSettingsHelp.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_help, Toast.LENGTH_SHORT).show()
        }
        binding.tvSettingsLogout.setOnClickListener {
            val logOutDialogFragment = LogOutDialogDialogFragment()
            val manager = requireActivity().supportFragmentManager
            logOutDialogFragment.show(manager, "logOutDialog")
        }
    }

    private fun showCurrent() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
            binding.settingsContainerChangePwd.visible(false)
            binding.tvSettingsLogout.visible(false)
            binding.viewHelp.visible(false)
        } else {
            binding.settingsContainerChangePwd.visible(true)
            binding.tvSettingsLogout.visible(true)
        }
    }

    private fun initUserData(userResponse: UserResponse) {

        binding.tvSettingsProfileFullName.text = userResponse.fullname

        if (userResponse.photo != "")
            Glide.with(binding.root).load(userResponse.photo)
                .into(binding.ivProfileSettings)
    }

    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")

        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle(getString(R.string.choose_lang))
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
    ) = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(AuthApi::class.java, token)
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)

        if (token.isNullOrEmpty()) {
            return AuthRepository(apiNoToken, userPreferences)
        } else {
            return AuthRepository(api, userPreferences)
        }
    }

    override fun setUserData(userResponse: UserResponse) {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()) {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        } else {
            initUserData(userResponse)
        }
    }

    override fun dataError(code: Int?) {
    }

}