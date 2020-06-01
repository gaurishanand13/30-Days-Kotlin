package com.ho.doctor

import android.util.Log
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference
import com.applozic.mobicomkit.api.notification.MobiComPushReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MyMessagingService"

    override fun onNewToken(token: String) {
        super.onNewToken(token);

        Applozic.getInstance(this).setDeviceRegistrationId(token)
        if (MobiComUserPreference.getInstance(this).isRegistered){
            try {
                val registrationResponse = RegisterUserClientService(this).updatePushNotificationId(token)
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }


    override fun onMessageReceived(p0: RemoteMessage) {


        if(p0.data.size>0){
            if(MobiComUserPreference.getInstance(this).isRegistered){
                MobiComPushReceiver.processMessageAsync(this,p0.data)
            }
        }
    }






    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}
