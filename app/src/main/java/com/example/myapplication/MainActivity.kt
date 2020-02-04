package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.net.Uri
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import android.widget.Toast
import androidx.core.net.toFile
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.content.FileProvider
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.provider.MediaStore
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val postURL: String = "http://172.27.221.4:8000/"
    private var imageURI: Uri? = null
    private var idFile: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.getStringExtra("image_data")!=null){
            val imagePath = intent.getStringExtra("image_data")
            imageURI = Uri.parse(imagePath)
            selected_image.setImageURI(imageURI)
        }



        capture_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, CaptureActivity::class.java))
        }

        galerie_button.setOnClickListener {
            startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
        }

        search_button.setOnClickListener {
            uploadImage(imageURI)
        }

    }

    fun uploadImage(imageURI: Uri?) {

            val path = imageURI!!.getPathString(this@MainActivity)

            val file = File(path)
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "img", file.getName(), RequestBody.create(
                    MediaType.parse("multipart/form-data"), file
                )
            )

            val retrofit = Retrofit.Builder()
                .baseUrl(postURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(PictureService::class.java)
            val pictureRequest = service.uploadImage(filePart)

            pictureRequest.enqueue(object : Callback<ResponseGet> {
                override fun onResponse(call: Call<ResponseGet>, response: Response<ResponseGet>) {
                    if (response.isSuccessful) {
                        response.body() // have your all data
                        val id = response.body()!!.id
                        val img = response.body()!!.img
                        val intent = Intent(this@MainActivity, ResultsActivity::class.java)
                        intent.putExtra("idImage", id.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.errorBody()!!.string(),
                            Toast.LENGTH_SHORT
                        ).show() // this will tell you why your api doesnt work most of time
                    }
                }
                override fun onFailure(call: Call<ResponseGet>, t: Throwable) {
                    Log.d("TAG", t.message)

                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT)
                        .show() // ALL NETWORK ERROR HERE
                }
            })
        }
    fun Uri.getPathString(context: Context): String {
        var path: String = ""

        context.contentResolver.query(
            this, arrayOf(MediaStore.Images.Media.DATA),
            null, null, null
        )?.apply {
            val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            moveToFirst()
            path = getString(columnIndex)
            close()
        }

        return path
    }
    }
