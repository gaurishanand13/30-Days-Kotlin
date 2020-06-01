package com.ho.clinic

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors.ClinicViewAllDoctorss
import com.ho.clinic.Retrofit.RetrofitClient
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_appointment.*
import java.io.File

class viewAppointment : AppCompatActivity() {


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
    var idList = ArrayList<String>()
    var selectedId : String? = null


    fun setUpTheSpinner(doctors : ClinicViewAllDoctorss){
        var listName  = ArrayList<String>()
        for(x in doctors!!.View_All_Doctors!!){
            listName.add(x.name)
            idList.add(x.id.toString())
        }
        selectedId = idList.get(0)

        val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                listName
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseDoctorSpinnner.adapter = adapter

        chooseDoctorSpinnner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedId = idList.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


    }

    fun assignDoctor(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Assigning...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Ion.with(this).load("http://13.124.117.70/api/v1.0/ClinicAssignAppointmentDocotor")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email", RetrofitClient.email)
                    .setBodyParameter("doctorid",selectedId)
                    .setBodyParameter(
                            "appointmentid",
                            RetrofitClient.homeCallDetails?.Appointments?.get(intent.getIntExtra("index",0))?.id.toString()
                    )
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {

                            val message = result?.get("message")?.asString
                            if(message.equals("Successfully assigned")){
                                Toast.makeText(this@viewAppointment,"Successfully Assigned!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else{
                                progressDialog.dismiss()
                                Toast.makeText(this@viewAppointment,message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

        }catch(e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }



    fun fetchDataForDoctors() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Ion.with(this).load("http://13.124.117.70/api/v1.0/ClinicViewAllDocotors")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email", RetrofitClient.email)
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            progressDialog.dismiss()
                            val response = Gson().fromJson<ClinicViewAllDoctorss>(result, ClinicViewAllDoctorss::class.java)
                            setUpTheSpinner(response)
                            appointmentSubmitButton.setOnClickListener {
                                //Set up the assignment button here
                                assignDoctor()
                            }
                        }
                    })

        }catch(e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_appointment)

        val index = intent.getIntExtra("index",0)




        Picasso.get().load(
                RetrofitClient?.homeCallDetails?.Patientphotopath
                        + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientprofilepic
        ).into(appointmentImageView)
        appointmentName.text =  "Patient : " + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientname
        appointmentTimings.text = RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.date + " " + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.time



        setSupportActionBar(appointmentTOolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        appointmentCall.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CALL_PHONE),123)
            }else{
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(
                        "tel:" + RetrofitClient.homeCallDetails?.Appointments?.get(index)?.patientmobile
                )))
            }
        }
        notesTextView.text = RetrofitClient.homeCallDetails?.Appointments?.get(index)?.patientnotes


        if(RetrofitClient.homeCallDetails?.Appointments?.get(index)?.doctorid==null){
            viewAppointmentTextView.visibility = View.GONE
            fetchDataForDoctors()
        }
        else{
            viewAppointmentNormalTextView.text = "Assigned Doctor"
            chooseDoctorSpinnner.visibility  = View.GONE
            appointmentSubmitButton.visibility = View.GONE
            viewAppointmentTextView.text = RetrofitClient.homeCallDetails?.Appointments?.get(index)?.doctorname
        }

    }
}
