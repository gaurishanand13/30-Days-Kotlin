package com.ho.clinic.ui.gallery

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
import com.ho.clinic.R
import com.ho.clinic.Retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_gallery.view.*

class GalleryFragment : Fragment() {
    var doctorImageView : ImageView?= null
    var clinicName : TextView? = null
    var clinicID : TextView? = null
    var clinicContactNumber : TextView? = null
    var clinicEmailAddress : TextView? = null
    var doctorQualification : TextView? = null
    var clinicLicenceDetails : TextView? = null
    var clinicFacilitiesAvailable : TextView?= null
    var clinicSpeciality : TextView? = null
    var insuranceAndLinkedCharges :  TextView? = null
    var AvailableTimings : TextView? = null
    var Address : TextView? = null
    var bankDetails : TextView? = null
    var paymentCharges: TextView? = null



    private var galleryViewModel: GalleryViewModel? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doctorImageView = view.doctorImageView
        clinicName = view.doctorName
        clinicContactNumber = view.doctorContactNumber
        clinicEmailAddress = view.doctorEmailAddress
        clinicID = view.clinicID
        clinicLicenceDetails = view.doctorLicenceDetails
        clinicSpeciality = view.speciality
        clinicFacilitiesAvailable = view.facilitiesAvailable
        insuranceAndLinkedCharges = view.insuranceAndLinkedCharges
        AvailableTimings = view.doctorAvailableTimings
        bankDetails = view.bankDetails
        paymentCharges = view.paymentCharges
        Address = view.doctorAddress

        setUpTheView(view)

        super.onViewCreated(view, savedInstanceState)
    }


    private fun setUpTheView(view: View?) {

        Log.i("tagg set up", RetrofitClient.user.toString())
        Picasso.get().load(RetrofitClient.user?.pataintphotopath + RetrofitClient.user?.Clinic?.profilepic).into(doctorImageView)

        clinicName?.text = "Name : " + RetrofitClient.user?.Clinic?.name
        var content = SpannableString(clinicName?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Name : ".length,0)
        clinicName?.text = content


        clinicContactNumber?.text = "Contact Number : " + RetrofitClient.user?.Clinic?.mobile
        content = SpannableString(clinicContactNumber?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Contact Number : ".length,0)
        clinicContactNumber?.text = content


        clinicEmailAddress?.text = "Email : " + RetrofitClient.user?.Clinic?.email
        content = SpannableString(clinicEmailAddress?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Email : ".length,0)
        clinicEmailAddress?.text = content

        clinicID?.text = "Clinic id : " + RetrofitClient?.user?.Clinic?.clinicid
        content = SpannableString(clinicID?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Clinic id : ".length,0)
        clinicID?.text = content


        clinicLicenceDetails?.text = "Licence Details : " + RetrofitClient.user?.Clinic?.licence
        content = SpannableString(clinicLicenceDetails?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Licence Details : ".length,0)
        clinicLicenceDetails?.text = content

        AvailableTimings?.text = "Available Timings : " +  RetrofitClient.user?.Clinic?.availabetiming
        content = SpannableString(AvailableTimings?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Available Timings : ".length,0)
        AvailableTimings?.text = content

        clinicSpeciality?.text =  "Speciality : " + RetrofitClient.user?.Clinic?.specilities
        content = SpannableString(clinicSpeciality?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Speciality : ".length,0)
        clinicSpeciality?.text = content

        Address?.text = "Address : " + RetrofitClient.user?.Clinic?.address
        content = SpannableString(Address?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Address : ".length,0)
        Address?.text = content


        clinicFacilitiesAvailable?.text = "Facilities Available : " +  RetrofitClient.user?.Clinic?.facilities
        content = SpannableString(clinicFacilitiesAvailable?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Facilities Available : ".length,0)
        clinicFacilitiesAvailable?.text = content


        insuranceAndLinkedCharges?.text = "Insurance & Linked Charges : " +  RetrofitClient.user?.Clinic?.insurance
        content = SpannableString(insuranceAndLinkedCharges?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Insurance & Linked Charges : ".length,0)
        insuranceAndLinkedCharges?.text = content

        bankDetails?.text = "Bank Details : " +  RetrofitClient.user?.Clinic?.bankdetails
        content = SpannableString(bankDetails?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Bank Details : ".length,0)
        bankDetails?.text = content


        paymentCharges?.text = "Payment Charges : " +  RetrofitClient.user?.Clinic?.appointmentscharges
        content = SpannableString(paymentCharges?.text)
        content.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),0,"Payment Charges : ".length,0)
        paymentCharges?.text = content
    }
}