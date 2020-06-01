package com.ho.doctor

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.ho.doctor.Retrofit.RetrofitClient
import com.ho.doctor.Retrofit.models.login.LoginDataClass
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


class Login : AppCompatActivity() {

    fun isOnline(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }


    fun isEmailValid(email: String?): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(loginToolbar)

        val content = SpannableString("Forgot Password")
        content.setSpan(UnderlineSpan(),0,"Forgot Password".length,0)
        forgotpassword.setText(content)
        forgotpassword.setOnClickListener {
            //Move to forgot password activity
            startActivity(Intent(this,forgotPassword::class.java))
        }


        loginButton.setOnClickListener {
            if(emailLoginEditText.text.toString().trim().isEmpty() || passwordLoginEditText.text.trim().toString().isEmpty()) {
                Toast.makeText(this,"Enter email and password both",Toast.LENGTH_SHORT).show()
            }
            else if(!isOnline()){
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }else if(!isEmailValid(emailLoginEditText.text.toString())){
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
            }
            else{
                tryLoginThroughION()
            }
        }


        registerTextView.setOnClickListener {
            startActivity(Intent(this,Registration::class.java))
        }


    }


    fun tryLoginThroughION(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        try {
            Ion.with(this).load("http://13.124.117.70/api/v1.0/Doctorlogin")
                    .setBodyParameter("email",emailLoginEditText.text.trim().toString())
                    .setBodyParameter("password",passwordLoginEditText.text.trim().toString())
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject>{
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
                                Toast.makeText(this@Login,"Error - ${response?.message}",Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }catch (e : Exception){
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }

    private fun tryLogin() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val jsonObject = JsonObject()
        jsonObject.addProperty("email",emailLoginEditText.text.trim().toString())
        jsonObject.addProperty("password",passwordLoginEditText.text.trim().toString())

        val call = RetrofitClient.retrofitService?.login(jsonObject)
        call?.enqueue(object : retrofit2.Callback<LoginDataClass>{
            override fun onFailure(call: Call<LoginDataClass>, t: Throwable) {
                progressDialog.dismiss()
                Log.i("tagg",t.message)
                Toast.makeText(this@Login,"Error - ${t.message}",Toast.LENGTH_SHORT).show()
            }

            /**
             *
             */
            override fun onResponse(call: Call<LoginDataClass>, response: Response<LoginDataClass>) {
                Log.i("response code",response.code().toString())
                Log.i("response code",response.toString())
                val response = response.body()
                Log.i("tagg response",response.toString())
                if(response?.message.equals("Login Successfully")){
                    val editor = getSharedPreferences("User_Data", Context.MODE_PRIVATE).edit()
                    editor.putString("User_email", response?.User?.email)
                    editor.putString("User_token", response?.User?.token_id)
                    editor.putString("name", response?.User?.name)
                    editor.apply()
                    editor.commit()
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
                    Toast.makeText(this@Login,"Error - ${response?.message}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}