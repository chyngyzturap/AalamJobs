package com.pharos.aalamjobs.data.network

import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.data.responses.*
import com.pharos.aalamjobs.ui.jobs.model.JobId
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface JobsApi {

    @GET("/api/jobs/all/")
    suspend fun getJobs(
        @Query ("search") q: String,
        @Query ("page") page: Int,
    ): JobsResponse

    @GET("/api/jobs/all/")
    suspend fun getJobsFiltered(
        @Query ("page") page: Int,
        @Query ("country") country: String,
        @Query ("city") city: String,
        @Query ("sector") sector: String

        ): JobsResponse



    @GET("/api/jobs/favorites/")
    suspend fun getFavoriteJobs(): FavJobsResponse

    @GET("/api/countries/")
    suspend fun getCountries(): CountryResponse

    @GET("/api/cities/")
    suspend fun getCities(): CityResponse

    @GET("/api/sectors/")
    suspend fun getSectors(): SectorResponse

    @GET("/api/currencies/")
    suspend fun getCurrencies(): CurrencyResponse

    @GET("/api/types/employment/")
    suspend fun getEmploymentTypes(): EmploymentTypeResponse

    @GET("/api/jobs/{id}/")
    suspend fun getJobById(
        @Path("id") storeId: Int
    ): Jobs

    @POST("logout")
    suspend fun logout(): ResponseBody

    @POST("/api/users/favorites/")
    suspend fun setFavorite(
//        @Header("Authorization") token: String,
        @Body jobId: JobId
    )

    @POST("/api/users/favorites/")
    suspend fun setFavoriteFromDetail(
        @Header("Authorization") token: String,
        @Body jobId: JobId
    )

    @DELETE("/api/users/favorites/{job}/")
    suspend fun deleteFavorite(@Path("job") job: Int)

    @DELETE("/api/users/favorites/{job}/")
    suspend fun deleteFavoriteFromDetail(
        @Path("job") job: Int,
        @Header("Authorization") token: String,
    )

    companion object {

        private const val BASE_URL = "http://165.227.143.167:9000"

        operator fun invoke(): JobsApi {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JobsApi::class.java)
        }
    }
}