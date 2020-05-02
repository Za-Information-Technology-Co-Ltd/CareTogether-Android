package com.za.caretogether.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContactModel :Serializable{

    @SerializedName("lat")
    @Expose
    lateinit var lat : String

    @SerializedName("lon")
    @Expose
    lateinit var long : String

    constructor(lat: String, long: String) {
        this.lat = lat
        this.long = long
    }
}