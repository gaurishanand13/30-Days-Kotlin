package com.ho.doctor

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.ho.doctor.Retrofit.RetrofitClient
import com.ho.doctor.Retrofit.models.register.register
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Response

class forgotPassword : AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun isOnline(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setSupportActionBar(forgotpasswordToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        subitForgotPassword.setOnClickListener {


            if(emailForgotPassword.text.toString().isEmpty()){
                Toast.makeText(this,"Enter email address",Toast.LENGTH_SHORT).show()
            }else if(!isOnline()){
                Toast.makeText(this,"No Internet Connection!",Toast.LENGTH_SHORT).show()
            }else{
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Wait...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val call = RetrofitClient.retrofitService?.forgotPassword(emailForgotPassword.text.trim().toString())
                call?.enqueue(object : retrofit2.Callback<register>{
                    override fun onFailure(call: Call<register>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<register>, response: Response<register>) {
                        val response = response.body()
                        progressDialog.dismiss()
                        if(response?.message.equals("All ready Forgot password Requrest sent") || response?.message.equals("Forgot password Requrest sent")){
                            progressDialog.dismiss()
                            Toast.makeText(this@forgotPassword,"Forgot password Requrest sent!",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            progressDialog.dismiss()
                            Log.i("error in response",response?.message)
                            Toast.makeText(this@forgotPassword,"Error - ${response?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}
