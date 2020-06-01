package com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors

import com.google.gson.annotations.SerializedName

data class ClinicViewAllDoctorss(
    val Doctorphotopath: String,
    @SerializedName("View All Doctors")
    var View_All_Doctors: ArrayList<ViewAllDoctor>,
    val message: String
)