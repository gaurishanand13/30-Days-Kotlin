package com.ho.doctor

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

//open class MyFirebaseInstanceIdService : MyFirebaseInstanceIdService() {
//    //this method will be called
//    //when the token is generated
//    fun onTokenRefresh() {
//
//
//        //now we will have the token
//        val token: String = FirebaseInstanceId.getInstance().getToken()!!
//
//        //for now we are displaying the token in the log
//        //copy it as this method is called only when the new token is generated
//        //and usually new token is only generated when the app is reinstalled or the data is cleared
//        Log.d("MyRefreshedToken", token)
//    }
//}