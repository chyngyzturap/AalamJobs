package com.pharos.aalamjobs.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.databinding.LayoutLogOutBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.base.BaseDialogFragment
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LogOutDialogDialogFragment: BaseDialogFragment<AuthViewModel, LayoutLogOutBinding, AuthRepository>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                userPreferences.clear()
            }

            launch {
                context?.let {
                    CvDatabase(it).getPersonalInfoDao().deleteAllFromPersonal()
                    CvDatabase(it).getContactInfoDao().deleteAllFromContacts()
                    CvDatabase(it).getEducationDao().deleteAllFromEducation()
                    CvDatabase(it).getEducationDao().deleteAllFromEducation2()
                    CvDatabase(it).getEducationDao().deleteAllFromEducation3()
                    CvDatabase(it).getLangDao().deleteAllFromMotherLanguage()
                    CvDatabase(it).getLangDao().deleteAllFromOtherLanguage()
                    CvDatabase(it).getLangDao().deleteAllFromOtherLanguage2()
                    CvDatabase(it).getLangDao().deleteAllFromOtherLanguage3()
                    CvDatabase(it).getExperienceDao().deleteAllFromExperience()
                    CvDatabase(it).getExperienceDao().deleteAllFromExperience2()
                    CvDatabase(it).getExperienceDao().deleteAllFromExperience3()
                }
            }
            requireActivity().startNewActivity(SplashActivity::class.java)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().onBackPressed()
            dismiss()
        }

    }

    override fun getViewModel()= AuthViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = LayoutLogOutBinding.inflate(inflater, container, false)

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
}