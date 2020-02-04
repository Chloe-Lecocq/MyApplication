package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.myapplication.R
import kotlinx.android.synthetic.main.capture_view.*
import kotlinx.android.synthetic.main.capture_view.image_view
import kotlinx.android.synthetic.main.capture_view.validate_btn
import kotlinx.android.synthetic.main.gallery_view.*

class CaptureActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    val PIC_CROP = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capture_view)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Prendre une photo"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)


        //button click
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

        //BUTTON CLICK
        validate_btn.setOnClickListener {
            val intent = Intent(this@CaptureActivity, MainActivity::class.java)
            intent.putExtra("image_data", image_uri.toString())
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
}

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAPTURE_CODE){
            performCrop()
        }
        if (resultCode == Activity.RESULT_OK && requestCode == PIC_CROP){
            image_uri = data?.data
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