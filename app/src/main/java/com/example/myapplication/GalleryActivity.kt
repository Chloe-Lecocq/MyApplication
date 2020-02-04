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
import android.content.ActivityNotFoundException












class GalleryActivity : AppCompatActivity() {

    var image_uri: Uri? = null
    val PIC_CROP = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_view)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Sélectionner une photo"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //BUTTON CLICK
        img_pick_btn.setOnClickListener {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        validate_btn.setOnClickListener {
            val intent = Intent(this@GalleryActivity, MainActivity::class.java)
            intent.putExtra("image_data", image_uri.toString())
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        image_uri = data?.data
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            performCrop()
        }
        if (requestCode == PIC_CROP){
            image_view.setImageURI(image_uri)
        }
    }



    fun performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            val cropIntent = Intent("com.android.camera.action.CROP")
            //indicate image type and Uri
            cropIntent.setDataAndType(image_uri, "image/*")
            //set crop properties
            cropIntent.putExtra("crop", "true")
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            //indicate output X and Y
            cropIntent.putExtra("outputX", 1036)
            cropIntent.putExtra("outputY", 1036)
            //retrieve data on return
            cropIntent.putExtra("return-data", true)

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP)


        } catch (anfe: ActivityNotFoundException) {
            //display an error message
            val errorMessage = "Whoops - your device doesn't support the crop action!"
            val toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
            toast.show()
        }

    }
}