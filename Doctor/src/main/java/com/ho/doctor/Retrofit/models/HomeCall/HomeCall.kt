package com.ho.doctor.Retrofit.models.HomeCall

data class HomeCall(
    val Appointments: ArrayList<Appointment>,
    val Patientphotopath: String,
    val Prescriptionpath: String,
    val message: String
)