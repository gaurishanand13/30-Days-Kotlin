package com.ho.clinic.ui.slideshow

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.ho.clinic.RecyclerViewAdapters.homeRecyclerViewInterface
import com.ho.clinic.RecyclerViewAdapters.viewAllDoctorsAdapter
import com.ho.clinic.RecyclerViewAdapters.viewAllDoctorsAdapterInterface
import com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors.ClinicViewAllDoctorss
import com.ho.clinic.Retrofit.RetrofitClient
import com.ho.clinic.doctorProfileActivity
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.android.synthetic.main.fragment_slideshow.view.*

class SlideshowFragment : Fragment() {

    var recylerView : RecyclerView? = null


    fun setTheSearchView(adapter : viewAllDoctorsAdapter){
        search_view.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!=null){
                    Log.i("i cam here","Hello")
                    adapter.updateRecyclerView(search_view.text.toString())
                }
                else{
                    Log.i("i cam here","S is null")
                    adapter.updateRecyclerView("")
                }
            }
        })
    }

    fun fetchDataForDoctors() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Ion.with(context).load("http://13.124.117.70/api/v1.0/ClinicViewAllDocotors")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email", RetrofitClient.email)
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            progressDialog.dismiss()
                            val response = Gson().fromJson<ClinicViewAllDoctorss>(result, ClinicViewAllDoctorss::class.java)
                            RetrofitClient.allDoctorDetails = response
                            viewAllDoctorsRecylerView.layoutManager = LinearLayoutManager(context)

                            val myInterface = object  : viewAllDoctorsAdapterInterface{
                                override fun makeACall(number: String) {
                                    if(ActivityCompat.checkSelfPermission(context!!,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                                        requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE),123)
                                    }else{
                                        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)))
                                    }
                                }

                                override fun moveToDoctorsProfile(id: Int?) {
                                    val intent = Intent(context,doctorProfileActivity::class.java)
                                    intent.putExtra("index",id)
                                    startActivity(intent)
                                }

                            }


                            val adapter = viewAllDoctorsAdapter(context!!,myInterface)
                            viewAllDoctorsRecylerView.adapter = adapter
                            setTheSearchView(adapter)

                        }
                    })

        }catch(e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recylerView = view.viewAllDoctorsRecylerView
        fetchDataForDoctors()
        super.onViewCreated(view, savedInstanceState)
    }

    private var slideshowViewModel: SlideshowViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }
}