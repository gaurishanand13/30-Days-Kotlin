package com.ho.clinic

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ho.clinic.Retrofit.DataClass.ClinicProfile.ClinicProfile
import com.ho.clinic.Retrofit.RetrofitClient
import com.ho.clinic.ui.splashScreen
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.onesignal.OneSignal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{
    var drawer: DrawerLayout? = null
    var navController: NavController? = null

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        fetchClinicDetails()

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController!!, drawer)
        NavigationUI.setupWithNavController(navigationView, navController!!)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, drawer)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawer!!.closeDrawers()
        when (item.itemId) {
            R.id.viewProfile -> navController!!.navigate(R.id.nav_gallery)
            R.id.viewDoctors -> navController!!.navigate(R.id.viewDoctors)
            R.id.logout -> {
                val pref = getSharedPreferences("User_Data", Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.remove("User_email")
                editor.remove("User_token")
                editor.commit()
                Toast.makeText(this, "Logout done", Toast.LENGTH_SHORT).show()
                val i = Intent(this@MainActivity, splashScreen::class.java)
                startActivity(i)
                finish()
                return true
            }
        }
        return false
    }

    fun fetchClinicDetails(){
        val editor = getSharedPreferences("User_Data", Context.MODE_PRIVATE)
        RetrofitClient.email = editor.getString("User_email", "").toString()
        RetrofitClient.name = editor.getString("name","").toString()
        RetrofitClient.header_token = editor.getString("User_token", "").toString()
        fetchClinicDetailsFromServer()
    }


    fun setNavigationHeader(){
        val headerView = nav_view.getHeaderView(0)
        val headerViewImage = headerView.findViewById<ImageView>(R.id.headerViewImage)
        val headerViewEmail = headerView.findViewById<TextView>(R.id.headerViewEmail)
        val headerViewName = headerView.findViewById<TextView>(R.id.headerViewName)
        Picasso.get().load(RetrofitClient.user?.pataintphotopath + RetrofitClient.user?.Clinic?.profilepic).into(headerViewImage)
        headerViewEmail.text = RetrofitClient.user?.Clinic?.email
        headerViewName.text = RetrofitClient.user?.Clinic?.name
    }


    fun fetchClinicDetailsFromServer() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        try {
            Ion.with(this).load("http://13.124.117.70/api/v1.0/ClinicProfile")
                    .setHeader("Authorization", RetrofitClient.header_token)
                    .setBodyParameter("email", RetrofitClient.email)
                    .asJsonObject().setCallback(object : FutureCallback<JsonObject> {
                        override fun onCompleted(e: Exception?, result: JsonObject?) {
                            val response = Gson().fromJson<ClinicProfile>(result, ClinicProfile::class.java)
                            progressDialog.dismiss()
                            RetrofitClient.user = response
                            setNavigationHeader()
                            Log.i("response", response.toString())
                        }
                    })

        } catch (e: Exception) {
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }
}