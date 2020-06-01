package com.ho.doctor.Retrofit.models.register

data class User(
    val email: String,
    val id: Int,
    val name: String,
    val token_id: String
)