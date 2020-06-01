package com.ho.clinic.Retrofit.DataClass

data class User(
    val clinicid: String,
    val email: String,
    val id: Int,
    val mobile: String,
    val name: String,
    val profilepic: String,
    val token_id: String
)