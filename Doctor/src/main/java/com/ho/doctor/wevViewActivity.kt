package com.ho.doctor

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ho.doctor.Retrofit.RetrofitClient
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_wev_view.*
import java.io.File
import java.lang.Exception

class wevViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wev_view)



        val type = intent.getStringExtra("type")
        if(type.equals("pdf")){
            zoomableImageView.visibility =  View.GONE
            pdfView.visibility = View.VISIBLE
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(File(intent.getStringExtra("path")).toString()))
            intent.setType("application/pdf")
            startActivity(intent)
//            pdfView.fromUri()
//                    .defaultPage(0)
//                    .enableSwipe(true)
//                    .enableDoubletap(true)
//                    .swipeHorizontal(false)
//                    .spacing(10)
//                    .onError{
//                        Log.i("error in pdf",it.message)
//                        Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
//                    }
//                    .load()
        }
        else{
            zoomableImageView.visibility =  View.VISIBLE
            pdfView.visibility = View.GONE
            val file = File(intent.getStringExtra("path"))
            zoomableImageView.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
            Log.i("tagg","Hello")
        }
    }
}
