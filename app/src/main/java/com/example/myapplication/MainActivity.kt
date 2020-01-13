package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.getStringExtra("image_data")!=null){
            val image_path = intent.getStringExtra("image_data")
            val fileUri = Uri.parse(image_path)
            selected_image.setImageURI(fileUri)
        }



        capture_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, CaptureActivity::class.java))
        }

        galerie_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
        }

        search_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, ResultsActivity::class.java))
        }

    }


}
