package com.ho.clinic

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ho.clinic.Retrofit.DataClass.ClinicPatientProfile.ClinicPatientProfile
import com.ho.clinic.Retrofit.RetrofitClient
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_patient_profile.*
import java.lang.Exception

class patientProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)

        setSupportActionBar(patientProfileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val patientID = intent.getStringExtra("patientID")
        findPatientDetails(patientID,RetrofitClient.email)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }



    private fun findPatientDetails(patientID: String, email: String?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Log.i("details" ,  """
                ${ RetrofitClient.header_token},
                ${ RetrofitClient.email},
                ${patientID}
            """.trimIndent())
            Ion.with(this).load("http://13.124.117.70/api/v1.0/ClinicPatientProfile")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email",email)
                    .setBodyParameter("patientid",patientID.toString())
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            val response = Gson().fromJson<ClinicPatientProfile>(result,ClinicPatientProfile::class.java)
                            Log.i("responsee patient",response.toString())
                            Picasso.get().load(response.pataintphotopath + response.Patient.profilepic).into(patientImageView)

                            patientName.text = "Name : " + response.Patient.name
                            var content = SpannableString(patientName.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Name : ".length,0)
                            patientName.text = content


                            patientContactNumber.text = "Contact Number : " + response.Patient.mobilenumber
                            content = SpannableString(patientContactNumber.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Contact Number : ".length,0)
                            patientContactNumber.text = content

                            patientEmailAddress.text = "Email : " + response.Patient.email
                            content = SpannableString(patientEmailAddress.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Email : ".length,0)
                            patientEmailAddress.text = content

                            patientGender.text = "Gender : " + response.Patient.gender
                            content = SpannableString(patientGender.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Gender : ".length,0)
                            patientGender.text = content

                            patientData.text = "Date : " + response.Patient.dob
                            content = SpannableString(patientData.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Date : ".length,0)
                            patientData.text = content

                            patientBloodGroup.text = "Blood Group : " +  response.Patient.bloodgroup
                            content = SpannableString(patientBloodGroup.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Blood Group : ".length,0)
                            patientBloodGroup.text = content

                            patientWeight.text =  "Weight : " + response.Patient.weight
                            content = SpannableString(patientWeight.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Weight : ".length,0)
                            patientWeight.text = content

                            patientHeight.text = "Height : " + response.Patient.height
                            content = SpannableString(patientHeight.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Height : ".length,0)
                            patientHeight.text = content


                            patientAddress.text = "Address : " + response.Patient.address
                            content = SpannableString(patientAddress.text)
                            content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Address : ".length,0)
                            patientAddress.text = content

                            progressDialog.dismiss()
                        }
                    })

        }catch (e : Exception){
            Toast.makeText(this,"Error - ${e.message}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
