package com.ho.clinic.ui.home

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ho.clinic.R
import com.ho.clinic.RecyclerViewAdapters.homeRecyclerViewAdapter
import com.ho.clinic.RecyclerViewAdapters.homeRecyclerViewInterface
import com.ho.clinic.Retrofit.DataClass.HomeCall.HomeCall
import com.ho.clinic.Retrofit.RetrofitClient
import com.ho.clinic.doctorProfileActivity
import com.ho.clinic.patientProfileActivity
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {


    var recyclerView : RecyclerView? = null
    private var homeViewModel: HomeViewModel? = null

    fun getInterface() : homeRecyclerViewInterface {
        val myInterface = object  : homeRecyclerViewInterface {
            override fun moveToPatientProfile(id: String?) {
                val intent = Intent(context, patientProfileActivity::class.java)
                intent.putExtra("patientID",id)
                startActivity(intent)
            }
            override fun makeACall(number: String) {
                if(ActivityCompat.checkSelfPermission(context!!,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE),123)
                }else{
                    startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)))
                }
            }

        }
        return myInterface
    }



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        fetchUserDetails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.homeRecyclerView

        //fetch all the details of the doctor
        fetchUserDetails()
    }

    fun fetchUserDetails() {

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Ion.with(context).load("http://13.124.117.70/api/v1.0/ClinicHomecall")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email",RetrofitClient.email)
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            val response = Gson().fromJson<HomeCall>(result,HomeCall::class.java)
                            RetrofitClient.homeCallDetails = response
                            progressDialog.dismiss()
                            OneSignal.sendTag("clinic_id",response.Clinic.id)
                            recyclerView?.layoutManager = LinearLayoutManager(context)
                            recyclerView?.adapter = homeRecyclerViewAdapter(context!!,getInterface())
                        }
                    })

        }catch(e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }
}