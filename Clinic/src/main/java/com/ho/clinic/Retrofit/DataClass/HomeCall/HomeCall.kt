package com.ho.clinic.Retrofit.DataClass.HomeCall

data class HomeCall(
    val Appointments: List<Appointment>,
    val Clinic : x,
    val Patientphotopath: String,
    val Prescriptionpath: String,
    val message: String
)
data class x(
        val id : String
)