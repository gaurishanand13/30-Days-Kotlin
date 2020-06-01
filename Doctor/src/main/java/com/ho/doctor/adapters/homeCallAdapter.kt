package com.ho.doctor.adapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference
import com.applozic.mobicomkit.api.account.user.User
import com.applozic.mobicomkit.listners.AlLoginHandler
import com.applozic.mobicomkit.listners.AlPushNotificationHandler
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity
import com.ho.doctor.AppointmentActitvity
import com.ho.doctor.Retrofit.RetrofitClient
import com.ho.doctor.Retrofit.models.patientProfile.patientProfile
import com.ho.doctor.viewAppointment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_item_view.view.*
import java.lang.Exception


interface homeRecyclerViewInterface{
    fun makeACall(number : String)
    fun moveToPatientProfile(id: Int?)
}


class homeRecyclerViewAdapter(var context : Context,val myInterface : homeRecyclerViewInterface?) : RecyclerView.Adapter<homeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(com.ho.doctor.R.layout.home_item_view,parent,false)
        return ViewHolder(listItem)
    }

    override fun getItemCount() = RetrofitClient.homeCallDetails?.Appointments!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(
                RetrofitClient.homeCallDetails?.Patientphotopath
                        + RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientprofilepic
        ).into(holder.itemView.homeItemViewImageView)

        val text = "Patient : " + RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientname
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(),"Patient : ".length,text!!.length,0)

        holder.itemView.homeItemNameTextView.text =content
        holder.itemView.homeItemNameTextView.setOnClickListener {
            myInterface?.moveToPatientProfile(RetrofitClient.homeCallDetails?.Appointments?.get(position)?.patientid!!.toInt())
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,viewAppointment::class.java)
            intent.putExtra("index",position)
            context.startActivity(intent)
        }

        holder.itemView.homeItemViewTimings.text =
                RetrofitClient.homeCallDetails?.Appointments?.get(position)?.date + " " + RetrofitClient.homeCallDetails?.Appointments?.get(position)?.time
        holder.itemView.homeItemClinicID.text = "Clinic : "+RetrofitClient.homeCallDetails?.Appointments?.get(position)?.clinicname

        holder.itemView.callClinic.setOnClickListener {
            myInterface?.makeACall(RetrofitClient?.homeCallDetails?.Appointments?.get(position)!!.clinicmobile)
        }
        holder.itemView.callDoctor.setOnClickListener {
            myInterface?.makeACall(RetrofitClient?.homeCallDetails?.Appointments?.get(position)!!.clinicmobile)
        }

        holder.itemView.homeItemViewCreatedAT.text = RetrofitClient.homeCallDetails?.Appointments?.get(position)?.created_at?.split("T")?.get(0)

        holder.itemView.chatDoctor.setOnClickListener {
            val progressDialog = ProgressDialog(context)
            progressDialog?.setMessage("Loading ...")
            progressDialog?.setCancelable(false)
            progressDialog?.show()


            val user = User()
            user.userId = "doctor" + RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.doctorid
            user.displayName = RetrofitClient.user?.DoctorProfile?.name
            user.authenticationTypeId = User.AuthenticationType.APPLOZIC.value
            user.email = RetrofitClient.email

            Applozic.connectUser(context,user,object : AlLoginHandler{
                override fun onFailure(registrationResponse: RegistrationResponse?, exception: Exception?) {
                    progressDialog?.dismiss()
                    Toast.makeText(context, "Error : " + registrationResponse, Toast.LENGTH_SHORT).show();
                }

                override fun onSuccess(registrationResponse: RegistrationResponse?, context: Context?) {
                    progressDialog?.dismiss()
                    val intent = Intent(context,ConversationActivity::class.java)
                    intent.putExtra(ConversationUIService.USER_ID,
                            "patient" + RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.patientid)
                    intent.putExtra(ConversationUIService.DISPLAY_NAME, RetrofitClient.homeCallDetails?.Appointments?.get(position)!!.patientname)
                    intent.putExtra(ConversationUIService.TAKE_ORDER, true)
                    context?.startActivity(intent)

                    if(MobiComUserPreference.getInstance(context).isRegistered){
                        Applozic.registerForPushNotification(context,Applozic.getInstance(context).deviceRegistrationId,object : AlPushNotificationHandler{
                            override fun onFailure(registrationResponse: RegistrationResponse?, exception: Exception?) {}

                            override fun onSuccess(registrationResponse: RegistrationResponse?) {

                            }
                        })
                    }
                }
            })
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}