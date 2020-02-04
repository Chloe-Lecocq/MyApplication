package com.example.myapplication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseGet {

    /**
     *
     * @return
     * The id
     */
    /**
     *
     * @param id
     * The id
     */
    @SerializedName("id")
    @Expose
    var id: Int? = null
    /**
     *
     * @return
     * The img
     */
    /**
     *
     * @param username
     * The img
     */
    @SerializedName("img")
    @Expose
    var img: String? = null

}