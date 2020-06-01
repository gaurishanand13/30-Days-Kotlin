package com.ho.doctor.Retrofit.models.HomeCall

data class Appointment(
    val clinicid: String,
    val created_at: String,
    val doctorid: String,
    val doctornotes: String?,
    val fees: String,
    val date : String,
    val id: Int,
    val patientid: String,
    val patientmobile: String,
    val patientname: String,
    val patientnotes: String,
    val patientprofilepic: String,
    val prescription: String?,
    val time: String,
    val updated_at: String,
    val clinicmobile: String,
    val clinicname: String
)