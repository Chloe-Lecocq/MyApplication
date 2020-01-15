package com.example.myapplication
import retrofit2.Call
import retrofit2.http.GET

interface PictureService {
    @GET("/courses")
    fun listPicture(): Call<List<Picture>>
    }
