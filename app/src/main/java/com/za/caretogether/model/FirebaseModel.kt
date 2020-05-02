package com.za.caretogether.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FirebaseModel :Serializable{

    @SerializedName("phone_number")
    @Expose
    lateinit var phone_number : String

    @SerializedName("token")
    @Expose
    lateinit var token : String

    constructor(phone_number: String, token: String) {
        this.phone_number = phone_number
        this.token = token
    }
}