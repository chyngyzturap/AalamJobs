package com.pharos.aalamjobs.ui.profile.edit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
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
import com.pharos.aalamjobs.utils.visible
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*


class EditProfileFragment : BaseFragment<AuthViewModel, FragmentEditProfileBinding, AuthRepository>(),
UserListener{
    private var file: File? = null
    var requestImage: MultipartBody.Part? = null
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(1, 1).getIntent(requireActivity())
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().nav_bottom.visible(false)

        viewModel.setUserListener(this)
        viewModel.getProfileInfo()

        binding.ivAddProfilePhoto.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }

        binding.ivBackpressed.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.ivDone.setOnClickListener {
            uploadData()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cropActivityResultLauncher = requireActivity().registerForActivityResult(cropActivityResultContract){
            it?.let { uri ->
                binding.ivProfilePhoto.setImageURI(uri)
                if (file == null){
                   file = fileFromContentUri(requireContext(), uri)
                }
                val requestFile : RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                requestImage = MultipartBody.Part.createFormData("photo", file!!.name, requestFile)
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

        if (userResponse.photo != "")
            Glide.with(binding.root).load(userResponse.photo)
                .into(binding.ivProfilePhoto)
    }

    @Suppress("DEPRECATION")
    private fun uploadImage(
        imageBytes: MultipartBody.Part?, mEmail: String, mFullname: String,
        mPosition: String, mCity: String, mCountry: String
    ) {
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
       viewModel.updateProfile(imageBytes, email, fullname, position, city, country)
    }

    private fun uploadData(){
        binding.progressbar.visible(true)
        val email = binding.etEmail.text.toString().trim()
        val fullname = binding.etFullName.text.toString().trim()
        val position = binding.etTitle.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()

        uploadImage(requestImage, email, fullname, position, city, country)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.nav_profile)
        }, 3000)
    }

    override fun setUserData(userResponse: UserResponse) {
        initUserData(userResponse)
    }

    override fun dataError(code: Int?) {
    }


    private fun fileFromContentUri(context: Context, uri: Uri?) : File {
        val fileExtension = getFileExtension(context, uri)
        val fileName = "aalam_jobs_avatar.jpg" + if (fileExtension != null) ".$fileExtension" else ""

        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val osStream = FileOutputStream(tempFile)
            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }

            inputStream?.let {
                copy(inputStream, osStream)
            }
            osStream.flush()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri?): String? {
        val fileType : String? = uri?.let { context.contentResolver.getType(it) }
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}