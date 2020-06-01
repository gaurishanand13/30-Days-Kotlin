package com.ho.doctor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.applozic.mobicomkit.Applozic
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.applozic.mobicomkit.api.account.user.User
import com.applozic.mobicomkit.listners.AlLoginHandler
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.ho.doctor.Retrofit.RetrofitClient
import com.ho.doctor.Retrofit.models.register.register
import com.obsez.android.lib.filechooser.ChooserDialog
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.activity_appointment_actitvity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext


class viewAppointment : AppCompatActivity() ,CoroutineScope{

    val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisor




    var myfile : File? = null
    var extension : String? = null
    var progressDialog : ProgressDialog? = null



    var path = ""
    private fun selectImage(context: Context) {

        val chooserDialog = ChooserDialog(this)
                .withFilter(false,false,"pdf","jpg","jpeg","png","PDF")
                .withStartFile(path)
                .withChosenListener(object : ChooserDialog.Result{
                    override fun onChoosePath(dir: String?, dirFile: File?) {
                        myfile = dirFile
                        Log.i("tagg",dir.toString())
                        extension = myfile!!.absolutePath.substring(myfile!!.absolutePath.lastIndexOf("."))
                        extension = extension!!.substring(1,extension!!.length)
                        Log.i("myExtension",extension)
                        pdf_or_image.visibility = View.VISIBLE
                        if(extension.equals("pdf") || extension.equals("PDF")){
                            pdf_or_image.setImageResource(R.drawable.pdf)
                        }
                        else{
                            pdf_or_image.setImageResource(R.drawable.image)
                        }
                    }
                })
                .build()
                .show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==123){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                startActivity(
                        Intent(
                                Intent.ACTION_CALL,Uri.parse(
                                "tel:" +
                                        RetrofitClient?.homeCallDetails?.Appointments?.get(
                                                intent.getIntExtra("index",0)
                                        )?.patientmobile.toString()
                        )))
            } else {
                Toast.makeText(this,"Grant the permission to call!",Toast.LENGTH_SHORT).show()
            }
            return
        }
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== Activity.RESULT_CANCELED) {
            when (requestCode) {
                1->{
                    if(resultCode== Activity.RESULT_OK && data!=null){

                        myfile = data.data!!.toFile()
                        extension = "jpg"
                        pdf_or_image.setImageResource(R.drawable.image)
                        pdf_or_image.visibility = View.VISIBLE
                    }
                }
                2->{
                    if(resultCode== Activity.RESULT_OK && data!=null){
                        val uri = data.data
                        myfile = uri!!.toFile()
                        extension = "pdf"
                        pdf_or_image.visibility = View.VISIBLE
                        pdf_or_image.setImageResource(R.drawable.pdf)
                        Log.i("extension",".pdf")
                    }
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }



    suspend fun submitUpdatesToServer(){
        progressDialog?.setMessage("Updating ...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        val map = HashMap<String,@JvmSuppressWildcards RequestBody>()
        map.put("email",RequestBody.create(MediaType.parse("text/plain"),RetrofitClient.email))
        map.put("id",RequestBody.create(
                MediaType.parse("text/plain"),
                RetrofitClient.homeCallDetails?.Appointments?.get(intent.getIntExtra("index",0))!!.id.toString())
        )
        map.put("doctornotes",RequestBody.create(MediaType.parse("text/plain"),appointmentDoctorNotesEditText.text.toString()))

        if(extension.equals("pdf") || extension.equals("PDF")){
            Log.i("came in exten"," Hi ")
            map.put("prescriptiontype",RequestBody.create(MediaType.parse("text/plain"),"pdf"))
            map.put("prescription\";filename=\"pp.pdf\"",RequestBody.create(MediaType.parse("application/pdf"),myfile))

            val call = RetrofitClient.retrofitService?.DoctorCompleteAppointment(map)
            call?.enqueue(object : retrofit2.Callback<register>{
                override fun onFailure(call: Call<register>, t: Throwable) {
                    progressDialog?.dismiss()
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<register>, response: Response<register>) {
                    progressDialog?.dismiss()
                    Log.i("response ww",response.toString())
                    if(response.body()?.message.equals("Appointment Updated")){
                        finish()
                        Toast.makeText(applicationContext,"Appointment Updated",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext,"Error - ${response.body()?.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }else{
            launch{
                val compressedImageFile = Compressor.compress(applicationContext, myfile!!) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.WEBP)
                    size(2_097_152) // 2 MB
                }

                map.put("prescriptiontype",RequestBody.create(MediaType.parse("text/plain"),"jpg"))
                map.put("prescription\";filename=\"pp.jpg\"",RequestBody.create(MediaType.parse("image/*"),compressedImageFile))

                val call = RetrofitClient.retrofitService?.DoctorCompleteAppointment(map)
                call?.enqueue(object : retrofit2.Callback<register>{
                    override fun onFailure(call: Call<register>, t: Throwable) {
                        progressDialog?.dismiss()
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<register>, response: Response<register>) {
                        progressDialog?.dismiss()
                        Log.i("response ww",response.toString())
                        if(response.body()?.message.equals("Appointment Updated")){
                            finish()
                            Toast.makeText(applicationContext,"Appointment Updated",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(applicationContext,"Error - ${response.body()?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            Log.i("came in exten"," Hello ")

        }
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_actitvity)

        setSupportActionBar(appointmentTOolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val index = intent.getIntExtra("index",0)


        progressDialog = ProgressDialog(this)

        Picasso.get().load(
                RetrofitClient?.homeCallDetails?.Patientphotopath +
                        RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientprofilepic
                ).into(appointmentImageView)
        appointmentName.text = RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientname
        appointmentTimings.text = RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.date+ " " +  RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.time
        appointmentPatientNotes.text = RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientnotes
        appointmentCall.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CALL_PHONE),123)
            }else{
                startActivity(Intent(Intent.ACTION_CALL,Uri.parse(
                        "tel:" + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.patientmobile.toString()
                )))
            }
        }
        appointmentChat.setOnClickListener {

            progressDialog = ProgressDialog(this)
            progressDialog?.setMessage("Loading ...")
            progressDialog?.setCancelable(false)
            progressDialog?.show()


            val user = User()
            user.userId = "doctor" + RetrofitClient.homeCallDetails?.Appointments?.get(index)!!.doctorid
            user.displayName = RetrofitClient.user?.DoctorProfile?.name
            user.authenticationTypeId = User.AuthenticationType.APPLOZIC.value
            user.email = RetrofitClient.email

            Applozic.connectUser(this,user,object : AlLoginHandler{
                override fun onFailure(registrationResponse: RegistrationResponse?, exception: Exception?) {
                    progressDialog?.dismiss()
                    Toast.makeText(this@viewAppointment, "Error : " + registrationResponse, Toast.LENGTH_SHORT).show();
                }

                override fun onSuccess(registrationResponse: RegistrationResponse?, context: Context?) {
                    progressDialog?.dismiss()
                    val intent = Intent(context,ConversationActivity::class.java)
                    intent.putExtra(ConversationUIService.USER_ID,
                            "patient" + RetrofitClient.homeCallDetails?.Appointments?.get(index)!!.patientid)
                    intent.putExtra(ConversationUIService.DISPLAY_NAME, RetrofitClient.homeCallDetails?.Appointments?.get(index)!!.patientname)
                    intent.putExtra(ConversationUIService.TAKE_ORDER, true)
                    startActivity(intent)


//                    if(MobiComUserPreference.getInstance(this@viewAppointment).isRegistered){
//                        Applozic.registerForPushNotification(this@viewAppointment,Applozic.getInstance(context).deviceRegistrationId,object : AlPushNotificationHandler {
//                            override fun onFailure(registrationResponse: RegistrationResponse?, exception: Exception?) {}
//
//                            override fun onSuccess(registrationResponse: RegistrationResponse?) {
//
//                            }
//                        })
//                    }

                }
            })
        }

        if(RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.doctornotes==null) {
            appointmentSelectPDF.visibility = View.VISIBLE
            appointmentSelectPDF.setOnClickListener {
                selectImage(this)
            }
            appointmentSubmitButton.visibility = View.VISIBLE
            appointmentSubmitButton.setOnClickListener {
                if(myfile==null && appointmentDoctorNotesEditText.text.isEmpty()){
                    Toast.makeText(this,"Insert prescription and notes",Toast.LENGTH_SHORT).show()
                }else if(myfile==null){
                    Toast.makeText(this,"Insert prescription",Toast.LENGTH_SHORT).show()
                }else if(appointmentDoctorNotesEditText.text.isEmpty()){
                    Toast.makeText(this,"Insert notes",Toast.LENGTH_SHORT).show()
                }
                else{
                    //Now try to submit it to the server
                    launch {
                        submitUpdatesToServer()
                    }
                }
            }
            appointmentDoctorNotesEditText.visibility = View.VISIBLE
            appointmentDoctorNotesTextView.visibility = View.GONE

        }
        else{
            Log.i("tag","icon  visible")
            appointmentSelectPDF.visibility = View.GONE
            appointmentSubmitButton.visibility = View.GONE
            appointmentDoctorNotesEditText.visibility = View.GONE
            appointmentDoctorNotesTextView.visibility = View.VISIBLE
            appointmentDoctorNotesTextView.text = RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.doctornotes
            pdf_or_image.visibility = View.VISIBLE
            if(RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.prescription!!.endsWith("pdf")){
                pdf_or_image.setImageResource(R.drawable.pdf)
            }else{
                pdf_or_image.setImageResource(R.drawable.image)
            }



            pdf_or_image.setOnClickListener {

                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(this, arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),123)
                    return@setOnClickListener
                }

//                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(
//                        RetrofitClient?.homeCallDetails?.Prescriptionpath + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.prescription
//                ))
//                startActivity(browserIntent)
//                val intent = Intent(this,wevViewActivity::class.java)
//                intent.putExtra("url",RetrofitClient?.homeCallDetails?.Prescriptionpath + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.prescription)
//                startActivity(intent)

                val progressBar = ProgressDialog(this)
                progressBar.setCancelable(false)
                progressBar.setMessage("File Downlaoding ...")
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressBar.progress = 0
                progressBar.max = 100
                progressBar.show()




                val config = PRDownloaderConfig.newBuilder()
                        .setDatabaseEnabled(true)
                        .build()
                PRDownloader.initialize(this, config)

                val url = RetrofitClient?.homeCallDetails?.Prescriptionpath + RetrofitClient?.homeCallDetails?.Appointments?.get(index)?.prescription

                val downloadURL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                val fileName = File(url).name
                val myFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
                val downloadId = PRDownloader
                        .download(url, downloadURL, fileName).build()
                        .setOnStartOrResumeListener {

                        }.setOnCancelListener{

                        }.setOnProgressListener{
                            progressBar.progress = ((it.currentBytes*100)/(it.totalBytes)).toInt()
                        }.start(object : OnDownloadListener{
                            override fun onDownloadComplete() {
                                progressBar.dismiss()
                                Toast.makeText(this@viewAppointment,"Download Complete",Toast.LENGTH_SHORT).show()

                                val pathName = downloadURL + "/" + fileName
                                val file = File(pathName)
                                MediaScannerConnection.scanFile(applicationContext, arrayOf<String>(file.getAbsolutePath()), null,
                                        object : MediaScannerConnection.OnScanCompletedListener {
                                            override fun onScanCompleted(path: String?, uri: Uri?) {
                                                val intent = Intent(Intent.ACTION_VIEW)
                                                val pathextensionlist = pathName.split('.')
                                                if(pathextensionlist.get(pathextensionlist.size-1).equals("pdf") || pathextensionlist.get(pathextensionlist.size-1).equals("PDF")){
                                                    intent.setDataAndType(
                                                            uri,
                                                            "application/pdf")
                                                }
                                                else{
                                                    intent.setDataAndType(
                                                            uri,
                                                            "image/*"
                                                    )
                                                }
                                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent)
                                            }
                                        })




                            }

                            override fun onError(error: Error?) {
                                Toast.makeText(this@viewAppointment,"Error in Downloading!\n Try Again",Toast.LENGTH_SHORT).show()
                            }
                        })
            }

        }
    }
}
