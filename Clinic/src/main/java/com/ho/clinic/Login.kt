package com.ho.clinic

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ho.clinic.Retrofit.DataClass.LoginDataClass
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    fun isOnline(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }



    fun tryLoginThroughION(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")
        progressDialog.setCancelable(false)
        progressDialog.show()


        try {
            Ion.with(this).load("http://13.124.117.70/api/v1.0/Cliniclogin")
                    .setBodyParameter("email",emailLoginEditText.text.trim().toString())
                    .setBodyParameter("password",passwordLoginEditText.text.trim().toString())
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            val response = Gson().fromJson<LoginDataClass>(result,LoginDataClass::class.java)
                            if(response?.message.equals("Login Successfully")){
                                val editor = getSharedPreferences("User_Data", Context.MODE_PRIVATE).edit()
                                editor.putString("User_email", response?.User?.email)
                                editor.putString("User_token", response?.User?.token_id)
                                editor.putString("name", response?.User?.name)
                                editor.apply()
                                editor.commit()
                                progressDialog.dismiss()
                                startActivity(Intent(this@Login,MainActivity::class.java))
                                finish()
                            }else{
                                if(response==null){
                                    Log.i("tagg 3","nullll")
                                }
                                if(response?.message==null){
                                    Log.i("tagg 4","nullll")
                                }
                                progressDialog.dismiss()
                                Toast.makeText(this@Login,"Error - ${response?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }catch (e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val content = SpannableString("Forgot Password")
        content.setSpan(UnderlineSpan(),0,"Forgot Password".length,0)
        forgotpassword.setText(content)
        forgotpassword.setOnClickListener {
            //Move to forgot password activity
            startActivity(Intent(this,forgotPassword::class.java))
        }


        loginButton.setOnClickListener {
            if(emailLoginEditText.text.toString().trim().isEmpty() || passwordLoginEditText.text.trim().toString().isEmpty()) {
                Toast.makeText(this,"Enter clinic id and password both",Toast.LENGTH_SHORT).show()
            }
            else if(!isOnline()){
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }
            else{
                tryLoginThroughION()
            }
        }


        registerTextView.setOnClickListener {
            startActivity(Intent(this,Registration::class.java))
        }

    }
}
