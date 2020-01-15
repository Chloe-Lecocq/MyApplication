package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.gallery_view.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ResultsActivity : AppCompatActivity() {

    private val url = "http://mobile-courses-server.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_layout)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Photos Similaires"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val service = retrofit.create(PictureService::class.java)
        val pictureRequest = service.listPicture()

        pictureRequest.enqueue(object : Callback<List<Picture>> {
            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>) {
                val allCourse = response.body()
                if (allCourse != null) {
                    Log.d("TAG", "HERE is ALL COURSES FROM HEROKU SERVER:")
                    println("HERE is ALL COURSES FROM HEROKU SERVER:")


                    for (c in allCourse){
                        Log.d("TAG", "HERE is ALL COURSES FROM HEROKU SERVER:")
                        println(" one course : ${c.title} : ${c.time} ")
                    }




                }
            }
            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
                error("KO")
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
