package com.ho.clinic.Retrofit

import com.ho.clinic.Retrofit.DataClass.ClinicProfile.ClinicProfile
import com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors.ClinicViewAllDoctorss
import com.ho.clinic.Retrofit.DataClass.HomeCall.HomeCall
import com.ho.clinic.Retrofit.DataClass.Register.RegisterDataClass
import okhttp3.Interceptor
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object RetrofitClient{
    var header_token = ""
    var name = ""
    var email = ""
    var user : ClinicProfile? = null
    var homeCallDetails : HomeCall? = null
    var allDoctorDetails : ClinicViewAllDoctorss? = null


    var httpClient = okhttp3.OkHttpClient.Builder().addInterceptor(Interceptor(){
        val original = it.request()
        val requestBuilder = original.newBuilder().addHeader("Authorization", header_token)
        val request = requestBuilder.build()
        return@Interceptor it.proceed(request)
    })
    val client = httpClient.build()
    val retrofit = Retrofit.Builder()
            .baseUrl("http://13.124.117.70/api/v1.0/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val retrofitService = retrofit.create(retrofitInterface::class.java)
}


data class retrotfitLogin(
        val email: String,
        val password: String
)

public interface retrofitInterface {


    @Multipart
    @POST("Clinicregister")
    fun register(
            @PartMap partMap: Map<String ,@JvmSuppressWildcards RequestBody>
    ) : retrofit2.Call<RegisterDataClass>



    @FormUrlEncoded
    @POST("Clinicforgotpassword")
    fun forgotPassword(
            @Field("email") email :String
    ) : retrofit2.Call<RegisterDataClass>
}