package com.ho.doctor.ui.gallery

import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ho.doctor.R
import com.ho.doctor.Retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import org.w3c.dom.Text

class GalleryFragment : Fragment() {

    var doctorImageView : ImageView?= null
    var doctorName : TextView? = null
    var doctorContactNumber : TextView? = null
    var doctorEmailAddress : TextView? = null
    var doctorQualification : TextView? = null
    var doctorLicenceDetails : TextView? = null
    var doctorSpeciality : TextView? = null
    var doctorAvailableTimings : TextView? = null
    var doctorAddress : TextView? = null
    var doctorGender : TextView? = null

    private var galleryViewModel: GalleryViewModel? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doctorImageView  = view.doctorImageView
        doctorContactNumber = view.doctorContactNumber
        doctorEmailAddress = view.doctorEmailAddress
        doctorQualification = view.doctorQualification
        doctorLicenceDetails = view.doctorLicenceDetails
        doctorAvailableTimings = view.doctorAvailableTimings
        doctorSpeciality = view.doctorSpeciality
        doctorName = view.doctorName
        doctorAddress = view.doctorAddress
        doctorGender = view.doctorGender
        setUpTheView(view)
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setUpTheView(view: View?) {

        Log.i("tagg set up",RetrofitClient.user.toString())
        Picasso.get().load(RetrofitClient.user?.Doctorphotopath + RetrofitClient.user?.DoctorProfile?.profilepic).into(doctorImageView)

        doctorName?.text = "Name : " + RetrofitClient.user?.DoctorProfile?.name
        var content = SpannableString(doctorName?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Name : ".length,0)
        doctorName?.text = content


        doctorContactNumber?.text = "Contact Number : " + RetrofitClient.user?.DoctorProfile?.mobile
        content = SpannableString(doctorContactNumber?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Contact Number : ".length,0)
        doctorContactNumber?.text = content


        doctorGender?.text = "Gender : " + RetrofitClient.user?.DoctorProfile?.gender
        content = SpannableString(doctorGender?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Gender : ".length,0)
        doctorGender?.text = content


        doctorEmailAddress?.text = "Email : " + RetrofitClient.user?.DoctorProfile?.email
        content = SpannableString(doctorEmailAddress?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Email : ".length,0)
        doctorEmailAddress?.text = content

        doctorQualification?.text = "Qualification : " + "MBBS"
        content = SpannableString(doctorQualification?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Qualification : ".length,0)
        doctorQualification?.text = content

        doctorLicenceDetails?.text = "Licence Details : " + RetrofitClient.user?.DoctorProfile?.licence
        content = SpannableString(doctorLicenceDetails?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Licence Details : ".length,0)
        doctorLicenceDetails?.text = content

        doctorAvailableTimings?.text = "Available Timings : " +  RetrofitClient.user?.DoctorProfile?.availibletiming
        content = SpannableString(doctorAvailableTimings?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Available Timings : ".length,0)
        doctorAvailableTimings?.text = content

        doctorSpeciality?.text =  "Speciality : " + RetrofitClient.user?.DoctorProfile?.speciality
        content = SpannableString(doctorSpeciality?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Speciality : ".length,0)
        doctorSpeciality?.text = content

        doctorAddress?.text = "Address : " + RetrofitClient.user?.DoctorProfile?.address
        content = SpannableString(doctorAddress?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Address : ".length,0)
        doctorAddress?.text = content
    }
}