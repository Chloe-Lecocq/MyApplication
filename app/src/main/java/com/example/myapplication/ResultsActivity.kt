package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import android.util.DisplayMetrics
import android.view.View
import androidx.core.view.ViewCompat
import retrofit2.converter.gson.GsonConverterFactory


class ResultsActivity : AppCompatActivity() {

    private val url = "http://127.0.0.1:8000/"

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
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var screen_width = displayMetrics.widthPixels

        val linearLayout = findViewById<LinearLayout>(R.id.linear_layout_base)

        list!!.forEachIndexed { index, element ->
            val constraintLayout = ConstraintLayout(this)


            val imageView = ImageView(this)
            imageView.id = ViewCompat.generateViewId()
            Glide.with(this).load(url + element.url).into(imageView)

            val urlImage = TextView(this)
            urlImage.text = element.marque
            urlImage.textSize = 14f

            val scoreImage = TextView(this)
            scoreImage.text = element.score.toString()
            scoreImage.textSize = 14f


            constraintLayout.addView(imageView)

            imageView.requestLayout()
            urlImage.requestLayout()

            scoreImage.setPadding(screen_width/2 + 30, 100, 0, 0);
            urlImage.setPadding(screen_width/2 + 30, 150, 0, 0);
            imageView.setPadding(0, 5, 0, 5);

            imageView.getLayoutParams().width = screen_width/2

            constraintLayout.addView(urlImage)
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
