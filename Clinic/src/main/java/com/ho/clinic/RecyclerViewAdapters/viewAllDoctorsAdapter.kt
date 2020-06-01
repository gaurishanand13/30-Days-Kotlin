package com.ho.clinic.RecyclerViewAdapters

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ho.clinic.R
import com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors.ClinicViewAllDoctorss
import com.ho.clinic.Retrofit.DataClass.ClinicViewAllDoctors.ViewAllDoctor
import com.ho.clinic.Retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_item_view.view.*
import kotlinx.android.synthetic.main.view_all_doctors_item_view.view.*

interface viewAllDoctorsAdapterInterface{
    fun makeACall(number : String)
    fun moveToDoctorsProfile(id: Int?)
}

class viewAllDoctorsAdapter(var context : Context, val myInterface : viewAllDoctorsAdapterInterface?) : RecyclerView.Adapter<viewAllDoctorsAdapter.ViewHolder>() {


    var currentList : ClinicViewAllDoctorss? = null

    init {
        currentList  = ClinicViewAllDoctorss(
                RetrofitClient?.allDoctorDetails?.Doctorphotopath!!,RetrofitClient?.allDoctorDetails?.View_All_Doctors!!,RetrofitClient?.allDoctorDetails?.message!!
        )
        Log.i("All Doctor Details",RetrofitClient?.allDoctorDetails.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.view_all_doctors_item_view,parent,false)
        return ViewHolder(listItem)
    }
    override fun getItemCount() = currentList?.View_All_Doctors!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(
                currentList?.Doctorphotopath
                        + currentList?.View_All_Doctors?.get(position)?.profilepic
        ).into(holder.itemView.viewAllDoctorsItemImageView)

        val text = currentList?.View_All_Doctors?.get(position)?.name

        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(),0,text!!.length,0)
        holder.itemView.viewAllDoctorsItemNameTextView.text =content


        holder.itemView.setOnClickListener {

            //First find the , position of this using id
            for(i in 0..RetrofitClient.allDoctorDetails!!.View_All_Doctors.size-1){
                if(RetrofitClient.allDoctorDetails!!.View_All_Doctors.get(i).id==currentList?.View_All_Doctors?.get(position)?.id){
                    myInterface?.moveToDoctorsProfile(i)
                }
            }
        }

        holder.itemView.viewAllDoctorsItemTimings.text = currentList?.View_All_Doctors?.get(position)?.availibletiming

        holder.itemView.viewAllDoctorsItemCall.setOnClickListener {
            currentList?.View_All_Doctors?.get(position)?.mobile?.let {
                it -> myInterface?.makeACall(it)
            }
        }

    }

    fun updateRecyclerView(name : String){
        val newListOfDoctorss = ArrayList<ViewAllDoctor>()
        if(name.equals("")){
            for(i in 0..RetrofitClient.allDoctorDetails!!.View_All_Doctors.size-1){
                newListOfDoctorss.add(RetrofitClient.allDoctorDetails!!.View_All_Doctors.get(i))
            }
        }
        else{
            for(i in 0..RetrofitClient.allDoctorDetails!!.View_All_Doctors.size-1){
                val x = RetrofitClient.allDoctorDetails!!.View_All_Doctors.get(i).name.toLowerCase()
                if(x.contains(name.toLowerCase())){
                    newListOfDoctorss.add(RetrofitClient.allDoctorDetails!!.View_All_Doctors.get(i))
                }
            }
        }
        currentList!!.View_All_Doctors = newListOfDoctorss
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}