package com.ho.clinic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ho.clinic.Retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_doctor_profile.*

class doctorProfileActivity : AppCompatActivity() {




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        setSupportActionBar(particularDoctorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        setUpTheView()
    }


    private fun setUpTheView() {

        val index = intent.getIntExtra("index",0)
        Log.i("tagg set up",RetrofitClient.user.toString())
        Log.i("tagg set up",index.toString())
        Log.i("tagg set up",RetrofitClient.allDoctorDetails?.View_All_Doctors?.size.toString())

        Picasso.get().load(RetrofitClient.allDoctorDetails?.Doctorphotopath + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.profilepic).into(doctorImageView)

        doctorName?.text = "Name : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.name
        var content = SpannableString(doctorName?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Name : ".length,0)
        doctorName?.text = content


        doctorContactNumber?.text = "Contact Number : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.mobile
        content = SpannableString(doctorContactNumber?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Contact Number : ".length,0)
        doctorContactNumber?.text = content


        doctorGender?.text = "Gender : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.gender
        content = SpannableString(doctorGender?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Gender : ".length,0)
        doctorGender?.text = content


        doctorEmailAddress?.text = "Email : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.email
        content = SpannableString(doctorEmailAddress?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Email : ".length,0)
        doctorEmailAddress?.text = content

        doctorQualification?.text = "Qualification : " + "MBBS"
        content = SpannableString(doctorQualification?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Qualification : ".length,0)
        doctorQualification?.text = content

        doctorLicenceDetails?.text = "Licence Details : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.licence
        content = SpannableString(doctorLicenceDetails?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Licence Details : ".length,0)
        doctorLicenceDetails?.text = content

        doctorAvailableTimings?.text = "Available Timings : " +  RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.availibletiming
        content = SpannableString(doctorAvailableTimings?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Available Timings : ".length,0)
        doctorAvailableTimings?.text = content

        doctorSpeciality?.text =  "Speciality : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.speciality
        content = SpannableString(doctorSpeciality?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Speciality : ".length,0)
        doctorSpeciality?.text = content

        doctorAddress?.text = "Address : " + RetrofitClient.allDoctorDetails?.View_All_Doctors?.get(index)?.address
        content = SpannableString(doctorAddress?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Address : ".length,0)
        doctorAddress?.text = content
    }


}
