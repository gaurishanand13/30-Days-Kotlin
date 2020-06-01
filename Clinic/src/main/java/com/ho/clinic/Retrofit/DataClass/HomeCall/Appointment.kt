package com.ho.clinic.Retrofit.DataClass.HomeCall

data class Appointment(
    val clinicid: String,
    val created_at: String,
    val date: String,
    val doctorid: String?,
    val doctormobile: String?,
    val doctorname: String?,
    val doctornotes: String?,
    val fees: String?,
    val id: Int,
    val patientid: String,
    val patientmobile: String,
    val patientname: String,
    val patientnotes: String,
    val patientprofilepic: String,
    val prescription: String,
    val time: String,
    val updated_at: String
)