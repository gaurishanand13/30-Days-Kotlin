package com.ho.doctor.Retrofit

import android.net.Uri
import com.google.gson.JsonObject
import com.ho.doctor.Retrofit.models.HomeCall.HomeCall
import com.ho.doctor.Retrofit.models.doctorProfile.DoctorProfile
import com.ho.doctor.Retrofit.models.login.LoginDataClass
import com.ho.doctor.Retrofit.models.patientProfile.patientProfile
import com.ho.doctor.Retrofit.models.register.register
import okhttp3.Interceptor
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object RetrofitClient{

    var uri : Uri? = null

    var header_token = ""
    var name = ""
    var email = ""
    var user : DoctorProfile? = null
    var homeCallDetails : HomeCall? = null


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


    @POST("Doctorlogin")
    fun login(
            @Body body: JsonObject
    ) : retrofit2.Call<LoginDataClass>



    @Multipart
    @POST("Doctorregister")
    fun register(
            @PartMap partMap: Map<String ,@JvmSuppressWildcards RequestBody>
    ) : retrofit2.Call<register>



    @Multipart
    @POST("postPrescription")
    fun postPrescription(
            @PartMap partMap: Map<String ,@JvmSuppressWildcards RequestBody>
    ) : retrofit2.Call<register>




    @FormUrlEncoded
    @POST("Doctorforgotpassword")
    fun forgotPassword(
            @Field("email") email :String
    ) : retrofit2.Call<register>



    @FormUrlEncoded
    @POST("DoctorHomecall")
    fun homeCall(
            @Field("email") email :String
    ) : retrofit2.Call<HomeCall>




    @FormUrlEncoded
    @POST("DoctorPatientProfile")
    fun PatientProfile(
            @Field("email") email :String,
            @Field("patientid") patientid:Int
    ) : retrofit2.Call<patientProfile>



    @Multipart
    @POST("DoctorCompleteAppointment")
    fun DoctorCompleteAppointment(
            @PartMap partMap: Map<String ,@JvmSuppressWildcards RequestBody>
    ) : retrofit2.Call<register>
}