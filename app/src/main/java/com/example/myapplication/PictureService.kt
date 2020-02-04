package com.example.myapplication
import retrofit2.Call
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.GET




interface PictureService {
    @Multipart
    @POST("/lookforit/")
    fun uploadImage(
        @Part img: MultipartBody.Part
    ): Call<ResponseGet>

    @GET("/lookforit/{id}")
    fun getResults(@Path("id") id: String): Call<List<Picture>>
}