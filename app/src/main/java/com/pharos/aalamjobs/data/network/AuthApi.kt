package com.pharos.aalamjobs.data.network

import com.pharos.aalamjobs.data.model.ChangePasswordModel
import com.pharos.aalamjobs.data.model.CreateUserModel
import com.pharos.aalamjobs.data.model.ForgotPasswordModel
import com.pharos.aalamjobs.data.model.TokenObtainPair
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.data.responses.PhoneCheckResponse
import com.pharos.aalamjobs.data.responses.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface AuthApi {

    @POST("/auth/jwt/create/")
    suspend fun login(@Body tokenObtainPair: TokenObtainPair) : LoginResponse

    @POST("/auth/users/")
    suspend fun createUser(@Body createUserModel: CreateUserModel): LoginResponse

    @PATCH("/auth/users/me/")
    @Multipart
    suspend fun updateProfile(
        @Part photo: MultipartBody.Part?,
        @Part("email") email: RequestBody?,
        @Part("fullname") fullname: RequestBody?,
        @Part("position") position: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("country") country: RequestBody?,
    )

    @GET("/auth/users/me/")
    suspend fun getProfileInfo(): UserResponse


    @FormUrlEncoded
    @POST("upload_image.php")
    fun uploadImage(@Field("photo") encodedImage: String?)

    @POST("/auth/users/set_password/")
    suspend fun changePassword(@Body changePasswordModel: ChangePasswordModel)

    @POST("/api/users/reset/password/")
    suspend fun forgotPassword(@Body forgotPasswordModel: ForgotPasswordModel)

    @FormUrlEncoded
    @POST("/api/phone/check/")
    suspend fun checkPhone(@Field("phone") phone: String): PhoneCheckResponse

    @POST("logout")
    suspend fun logout(): ResponseBody
}