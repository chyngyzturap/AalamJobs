package com.pharos.aalamjobs.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.UserResponse
import com.pharos.aalamjobs.databinding.FragmentEditProfileBinding
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.CHOOSE_IMAGE_REQUEST
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditProfileFragment : BaseFragment<AuthViewModel, FragmentEditProfileBinding, AuthRepository>(),
UserListener{

    var bitmap: Bitmap? = null
    val mUserResponse: UserResponse? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().nav_bottom.visible(false)

        viewModel.setUserListener(this)
        viewModel.getProfileInfo()

        binding.ivAddProfilePhoto.setOnClickListener {
            openImageChooser()
        }

        binding.ivBackpressed.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.ivDone.setOnClickListener {
            uploadData()
        }
    }


    private fun openImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выберите фото профиля!"),
            CHOOSE_IMAGE_REQUEST
        )
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_IMAGE_REQUEST)

        {
            val path = data!!.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, path)
                binding.ivProfilePhoto.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentEditProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(AuthApi::class.java, token)
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)
        return AuthRepository(api, userPreferences)
    }

    private fun initUserData(userResponse: UserResponse){
        binding.tvCity.editText?.setText(userResponse.city)
        binding.tvCountry.editText?.setText(userResponse.country)
        binding.tvTitle.editText?.setText(userResponse.position)
        binding.tvPhone.editText?.setText(userResponse.username)
        binding.tvEmail.editText?.setText(userResponse.email)
        binding.tvFullName.editText?.setText(userResponse.fullname)

        if (userResponse.photo.isNotEmpty())
            Glide.with(binding.root).load(userResponse.photo)
                .error(
                    ContextCompat.getDrawable(
                        binding.root.context, R.drawable.logo
                    )
                ).into(binding.ivProfilePhoto)
    }

    @Suppress("DEPRECATION")
    private fun uploadImage(imageBytes: ByteArray, mEmail: String, mFullname: String,
                            mPosition: String, mCity: String, mCountry: String) {

        val requestFile: RequestBody = create("image".toMediaTypeOrNull(), imageBytes)
        val body: MultipartBody.Part = createFormData("image", "image", requestFile)
        val fullname: RequestBody =
            mFullname.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val email: RequestBody =
            mEmail.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val position: RequestBody =
            mPosition.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val city: RequestBody =
            mCity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val country: RequestBody =
            mCountry.toRequestBody("multipart/form-data".toMediaTypeOrNull())
       viewModel.updateProfile(body, email, fullname, position, city, country)
//        mProgressBar.setVisibility(View.VISIBLE)
    }

    private fun uploadData(){
        val byteArrayOutputStream = ByteArrayOutputStream()
        val imageInByte = byteArrayOutputStream.toByteArray()

        val email = binding.etEmail.text.toString().trim()
        val fullname = binding.etFullName.text.toString().trim()
        val position = binding.etTitle.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()


        uploadImage(imageInByte, email, fullname, position, city, country)

        findNavController().navigate(R.id.nav_profile)
    }

    override fun setUserData(userResponse: UserResponse) {
        initUserData(userResponse)
    }

    override fun dataError(code: Int?) {
        TODO("Not yet implemented")
    }

    override fun setUserId(id: Int?, logo: String?) {
        TODO("Not yet implemented")
    }

    override fun updateUserSuccess() {
        TODO("Not yet implemented")
    }

    override fun quitDone() {
        TODO("Not yet implemented")
    }

}