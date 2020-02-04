package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.Image
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.results_layout.*
import android.graphics.Color.parseColor
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.DisplayMetrics
import android.view.View
import androidx.core.view.ViewCompat
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.converter.gson.GsonConverterFactory


class ResultsActivity : AppCompatActivity() {

    private val url = "http://172.27.221.4:8000/"

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
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PictureService::class.java)
        val pictureRequest = service.getResults(intent.getStringExtra("idImage")!!)

        pictureRequest.enqueue(object : Callback<List<Picture>> {
            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>) {
                val allCourse = response.body()
                createView(allCourse)
            }
            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
                Log.d("TAG", t.message)
            }
        })




    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun createView(list: List<Picture>?){
        // Get Screen metrics
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var screen_width = displayMetrics.widthPixels

        val linearLayout = findViewById<LinearLayout>(R.id.linear_layout_base)

        list!!.forEachIndexed { index, element ->
            val constraintLayout = ConstraintLayout(this)


            val imageView = ImageView(this)
            imageView.id = ViewCompat.generateViewId()
            Glide.with(this).load("http://172.27.221.4:8000/" + element.url).into(imageView)

            val scoreImage = TextView(this)
            scoreImage.text = element.score.toString()
            scoreImage.textSize = 14f

            constraintLayout.addView(imageView)

            imageView.requestLayout()

            scoreImage.setPadding(screen_width/2 + 30, 100, 0, 0);
            imageView.setPadding(0, 5, 0, 5);

            imageView.getLayoutParams().width = screen_width/2

            constraintLayout.addView(scoreImage)

            linearLayout.addView(constraintLayout)

            val v = View(this)
            v.setLayoutParams(
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2
                )
            )
            v.setBackgroundColor(Color.parseColor("#FFAB91"))

            linearLayout.addView(v)
        }
    }
}
