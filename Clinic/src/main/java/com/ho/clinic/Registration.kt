package com.ho.clinic

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ho.clinic.Retrofit.DataClass.Register.RegisterDataClass
import com.ho.clinic.Retrofit.RetrofitClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.available_timing_layout.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Registration : AppCompatActivity() {


//    fun uploadFile(sourceFileUri: String): Int {
//        var dos: DataOutputStream? = null
//        val lineEnd = "\r\n"
//        val twoHyphens = "--"
//        val boundary = "*****"
//        var bytesRead: Int
//        var bytesAvailable: Int
//        var bufferSize: Int
//        val buffer: ByteArray
//        val maxBufferSize = 1 * 1024 * 1024
//        try {
//
//            // open a URL connection to the Servlet
//            val fileInputStream = FileInputStream(sourceFile)
//            val url = URL(upLoadServerUri)
//
//            // Open a HTTP  connection to  the URL
//            conn = url.openConnection() as HttpURLConnection
//            conn.setDoInput(true) // Allow Inputs
//            conn.setDoOutput(true) // Allow Outputs
//            conn.setUseCaches(false) // Don't use a Cached Copy
//            conn.setRequestMethod("POST")
//            conn.setRequestProperty("Connection", "Keep-Alive")
//            conn.setRequestProperty("ENCTYPE", "multipart/form-data")
//            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
//            conn.setRequestProperty("uploaded_file", sourceFileUri)
//            conn.setRequestProperty("userNumber", usernumber)
//            dos = DataOutputStream(conn.getOutputStream())
//            dos.writeBytes(twoHyphens + boundary + lineEnd)
//            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
//                    + sourceFileUri + "\"" + lineEnd)
//            dos.writeBytes(lineEnd)
//
//            //    dos.writeBytes(twoHyphens + boundary + lineEnd);
//            //    dos.writeBytes("Content-Disposition: form-data;
//            //    number=\"userNumber\";
//            //    usernumber=\""+ usernumber + "\"" + lineEnd);
//            //    Log.d("number",usernumber);
//            //   dos.writeBytes(lineEnd);
//
//            // create a buffer of  maximum size
//            bytesAvailable = fileInputStream.available()
//            bufferSize = Math.min(bytesAvailable, maxBufferSize)
//            buffer = ByteArray(bufferSize)
//
//            // read file and write it into form...
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
//            while (bytesRead > 0) {
//                dos.write(buffer, 0, bufferSize)
//                bytesAvailable = fileInputStream.available()
//                bufferSize = Math.min(bytesAvailable, maxBufferSize)
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
//            }
//
//            // send multipart form data necesssary after file data...
//            dos.writeBytes(lineEnd)
//            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
//
//            // Responses from the server (code and message)
//            serverResponseCode = conn.getResponseCode()
//            val serverResponseMessage: String = conn.getResponseMessage()
//            Log.i("uploadFile", "HTTP Response is : "
//                    + serverResponseMessage + ": " + serverResponseCode)
//            if (serverResponseCode === 200) {
//                val response: InputStream = conn.getInputStream()
//                val jsonReply: String = convertStreamToString(response)
//                Log.d("reply", jsonReply)
//                runOnUiThread {
//                    val msg = """File Upload Completed.
//
// See uploaded file here :
//
// F:/wamp/wamp/www/uploads"""
//                    //   messageText.setText(msg);
//                    Toast.makeText(this, "File Upload Complete.", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            //close the streams //
//            fileInputStream.close()
//            dos.flush()
//            dos.close()
//        } catch (ex: MalformedURLException) {
//            dialog.dismiss()
//            ex.printStackTrace()
//            runOnUiThread { // messageText.setText("MalformedURLException Exception : check script url.");
//                Toast.makeText(this, "MalformedURLException", Toast.LENGTH_SHORT).show()
//            }
//            Log.e("Upload file to server", "error: " + ex.getMessage(), ex)
//        } catch (e: java.lang.Exception) {
//            dialog.dismiss()
//            e.printStackTrace()
//            runOnUiThread { // messageText.setText("Got Exception : see logcat ");
//                Toast.makeText(this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show()
//            }
//            Log.e("Upload file to server Exception", "Exception : " + e.message, e)
//        }
//        dialog.dismiss()
//        return serverResponseCode
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            123 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                } else {
                    Toast.makeText(this,"Grant the permission to continue!", Toast.LENGTH_SHORT).show()
                }
                return
            }
            125 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                } else {
                    Toast.makeText(this,"Grant the permission to continue!", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else->{
                //Nothing to perform
            }
        }
    }


    fun getImageFromImageView() : File? {
        try {

            val bitmap = (registerProfileImageView.getDrawable() as BitmapDrawable).bitmap
            val bao = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,bao)
            val ba = bao.toByteArray()
            val destination = File(Environment.getExternalStorageDirectory(), "profile.jpg")
            val fileOutputStream = FileOutputStream(destination)
            fileOutputStream.write(ba);
            fileOutputStream.close();
            return destination
        }catch (e : Exception){
            Toast.makeText(this,e.localizedMessage, Toast.LENGTH_SHORT).show()
            Log.i("error in image",e.message)
            Log.i("error in image",e.stackTrace.toString())
            e.printStackTrace()
        }
        return null
    }

    fun photoCaptureOrHandler(){
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), 123)
                }else{
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                }
            } else if (options[item] == "Choose from Gallery") {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 125)
                }else{
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode === Activity.RESULT_OK && data != null) {
                    registerProfileImageView.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
                1 -> if (resultCode === Activity.RESULT_OK && data != null) {
                    Log.i("tagg","hi")
                    val selectedImage = data.getData()
                    Picasso.get().load(selectedImage).into(registerProfileImageView)
                }
            }
        }
    }


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


    fun registerFunction(){
        val facilitesAvailable = facilitiesAvailable.text.toString()
        val insurantAndLinkedCharges = insuranceAndLinkedCharges.text.toString()
        val bankDetails = bankDetails.text.toString()
        val appointmenCharges = appointmentCharges.text.toString()
        val fullName = fullNameEditText.text.toString()//
        val mobileNumber = mobileNumberEditText.text.toString().trim()//
        val email = emailEditText.text.toString().trim()//
        val password = passwordEditText.text.toString().trim()//
        val licenceDetails = licenceDetailsEditText.text.toString()//
        val availableTimings = AvailableTimingsEditText.text.toString()//
        val speciality = specialityEditText.text.toString()//
        val address = addressEditText.text.toString()//
        val clinicID = clinicIDEditText.text.toString()//
        val image =  getImageFromImageView()


        if(image==null){
            Toast.makeText(this,"Please Fill all the details",Toast.LENGTH_SHORT).show()
            return
        }

        Log.i("fille",image.toString())

        if(fullName.isEmpty() || mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty() || licenceDetails.isEmpty()
                || availableTimings.equals("Available Timings") || speciality.isEmpty() || address.isEmpty() || clinicID.isEmpty()
                ||appointmenCharges.isEmpty() || bankDetails.isEmpty() || insurantAndLinkedCharges.isEmpty() || facilitesAvailable.isEmpty()){
            Toast.makeText(this,"Please Fill all the details", Toast.LENGTH_SHORT).show()
        }
        else{
            if(!isOnline()){
                Toast.makeText(this,"No Internet Connection ", Toast.LENGTH_SHORT).show()
            }else if(!isEmailValid(email)){
                Toast.makeText(this,"Invalid Email ", Toast.LENGTH_SHORT).show()
            }else if(image==null){
                Log.i("tagg","image was null")
                Toast.makeText(this,"Error! Try Again", Toast.LENGTH_SHORT).show()
            }else{
                //Now we can make a call at register
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Registering ...")
                progressDialog.setCancelable(false)
                progressDialog.show()


                val map = HashMap<String,@JvmSuppressWildcards RequestBody>()
                map.put("email", RequestBody.create(MediaType.parse("text/plain"),email))
                map.put("password", RequestBody.create(MediaType.parse("text/plain"),password))
                map.put("name", RequestBody.create(MediaType.parse("text/plain"),fullName))
                map.put("mobile", RequestBody.create(MediaType.parse("text/plain"),mobileNumber))
                map.put("clinicid", RequestBody.create(MediaType.parse("text/plain"),clinicID))
                map.put("licence", RequestBody.create(MediaType.parse("text/plain"),licenceDetails))
                map.put("specilities", RequestBody.create(MediaType.parse("text/plain"),speciality))
                map.put("facilities", RequestBody.create(MediaType.parse("text/plain"),facilitesAvailable))
                map.put("insurance", RequestBody.create(MediaType.parse("text/plain"),insurantAndLinkedCharges))
                map.put("availabetiming", RequestBody.create(MediaType.parse("text/plain"),availableTimings))
                map.put("bankdetails", RequestBody.create(MediaType.parse("text/plain"),bankDetails))
                map.put("appointmentscharges", RequestBody.create(MediaType.parse("text/plain"),appointmenCharges))
                map.put("address", RequestBody.create(MediaType.parse("text/plain"),address))
                map.put("profilepic\";filename=\"profile.png\"",RequestBody.create(MediaType.parse("image/*"),image))


                val call = RetrofitClient.retrofitService?.register(map)
                call?.enqueue(object : retrofit2.Callback<RegisterDataClass>{
                    override fun onFailure(call: Call<RegisterDataClass>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext,t.message, Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<RegisterDataClass>, response: Response<RegisterDataClass>) {
                        Log.i("responseee",response.toString())
                        val response = response.body()
                        progressDialog.dismiss()
                        if(response?.message.equals("New Clinic Register")){
                            val editor = getSharedPreferences("User_Data", Context.MODE_PRIVATE).edit()
                            editor.putString("User_email", response?.User?.email)
                            editor.putString("User_token", response?.User?.token_id)
                            editor.putString("name", response?.User?.name)
                            editor.apply()
                            editor.commit()
                            startActivity(Intent(this@Registration,MainActivity::class.java))
                            finish()
                        }else{
                            Log.i("error in response",response?.message.toString())
                            Toast.makeText(this@Registration,"Error - ${response?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    var startingDay = ""
    var endingDay = ""
    var startingTime = ""
    var endingTime = ""

    fun setUpTimingSpinner(view: View){
        val days = ArrayList<String>()
        days.add("Monday")
        days.add("Tuesday")
        days.add("Wednesday")
        days.add("Thursday")
        days.add("Friday")
        days.add("Saturday")
        days.add("Sunday")


        val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.ALL_DAYS,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.startingDaysSpinner.adapter = adapter

        view.startingDaysSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                startingDay = days.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        val adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.ALL_DAYS,
                android.R.layout.simple_spinner_item
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.endingDaySpinner.adapter = adapter2
        view.endingDaySpinner.setSelection(6)
        view.endingDaySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                endingDay = days.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    fun setUpTimePicker(view: View){
        view.startingTimePicker.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val hour: Int = c.get(Calendar.HOUR_OF_DAY)
            val minute: Int = c.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int) {
                    var time = ""
                    if(selectedHour>12){
                        time =  "${selectedHour-12}:$selectedMinute PM"
                    }
                    else{
                        if(selectedHour==12){
                            time =  "${selectedHour}:$selectedMinute PM"
                        }
                        else{
                            time =  "${selectedHour}:$selectedMinute AM"
                        }
                    }
                    view.startingTimePicker.text = time
                    startingTime = time
                }
            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        view.finalTimePicker.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val hour: Int = c.get(Calendar.HOUR_OF_DAY)
            val minute: Int = c.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(timePicker: TimePicker?, selectedHour: Int, selectedMinute: Int) {
                    var time = ""
                    if(selectedHour>12){
                        time =  "${selectedHour-12}:$selectedMinute PM"
                    }
                    else{
                        if(selectedHour==12){
                            time =  "${selectedHour}:$selectedMinute PM"
                        }
                        else{
                            time =  "${selectedHour}:$selectedMinute AM"
                        }
                    }
                    view.finalTimePicker.text = time
                    endingTime = time
                }
            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)



        AvailableTimingsEditText.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.available_timing_layout,frameLayoutRegistration,false)
            setUpTimePicker(dialogView)
            setUpTimingSpinner(dialogView)
            builder.setView(dialogView)
            val x = builder.create()
            dialogView.setTimeButton.setOnClickListener {
                if(dialogView.startingTimePicker.text.equals("FROM") || dialogView.finalTimePicker.text.equals("TO")){
                    Toast.makeText(this,"Choose Time",Toast.LENGTH_SHORT).show()
                }else{
                    AvailableTimingsEditText.text = "$startingDay-$endingDay  $startingTime-$endingTime"
                    x.dismiss()
                }
            }
            x.show()
        }


        setSupportActionBar(registrationToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)





        registerProfileImageView.setOnClickListener {
            photoCaptureOrHandler()
        }

        registerButtonRegister.setOnClickListener{
            registerFunction()
        }
    }
}
