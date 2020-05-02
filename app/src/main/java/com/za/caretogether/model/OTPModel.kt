package com.za.caretogether.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OTPModel :Serializable{

    @SerializedName("phone")
    @Expose
    lateinit var phone : String

    constructor(phone: String) {
        this.phone = phone
    }
}