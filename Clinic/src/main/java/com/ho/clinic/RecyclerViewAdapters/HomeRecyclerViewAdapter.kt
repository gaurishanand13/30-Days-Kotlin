package com.ho.clinic.RecyclerViewAdapters

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ho.clinic.R
import com.ho.clinic.Retrofit.RetrofitClient
import com.ho.clinic.viewAppointment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_item_view.view.*


interface homeRecyclerViewInterface{
    fun makeACall(number : String)
    fun moveToPatientProfile(id: String?)
}


class homeRecyclerViewAdapter(var context : Context, val myInterface : homeRecyclerViewInterface?) : RecyclerView.Adapter<homeRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.home_item_view,parent,false)
        return ViewHolder(listItem)
    }
    override fun getItemCount() = RetrofitClient.homeCallDetails?.Appointments!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(
                RetrofitClient.homeCallDetails?.Patientphotopath
                        + RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientprofilepic
        ).into(holder.itemView.homeItemViewImageView)


        val text = "Patient : "  + RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientname
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(),"Patient : ".length,text!!.length,0)
        holder.itemView.homeItemNameTextView.text =content


        holder.itemView.homeItemNameTextView.setOnClickListener {
            myInterface?.moveToPatientProfile(RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientid)
        }




        holder.itemView.setOnClickListener {
            val intent = Intent(context, viewAppointment::class.java)
            intent.putExtra("index",position)
            context.startActivity(intent)
        }

        holder.itemView.homeItemViewTimings.text = RetrofitClient.homeCallDetails?.Appointments?.get(position)?.date + " "+ RetrofitClient.homeCallDetails?.Appointments?.get(position)?.time

        holder.itemView.callPatient.setOnClickListener {
            myInterface?.makeACall(RetrofitClient?.homeCallDetails?.Appointments?.get(position)!!.patientmobile)
        }

        holder.itemView.homeItemViewCreatedAT.text = RetrofitClient.homeCallDetails?.Appointments?.get(position)?.created_at?.split("T")?.get(0)



        if(RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.doctorid==null)
        {
            holder.itemView.callDoctor.visibility = View.GONE
            holder.itemView.homeItemDoctorName.visibility = View.GONE
        }
        else{
            holder.itemView.homeItemDoctorName.text = "Doctor : " + RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.doctorname
                    holder.itemView.callDoctor.setOnClickListener {
                myInterface?.makeACall(RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.doctormobile!!)
            }
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}