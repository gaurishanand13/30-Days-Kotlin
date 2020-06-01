package com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors

data class ViewAllDoctor(
    val address: String,
    val availibletiming: String,
    val chat_user_id: Any,
    val clinicid: String,
    val email: String,
    val gender: String,
    val id: Int,
    val licence: String,
    val mobile: String,
    val name: String,
    val profilepic: String,
    val speciality: String
)