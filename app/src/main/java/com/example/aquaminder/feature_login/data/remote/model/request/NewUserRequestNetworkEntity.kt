package com.example.aquaminder.feature_login.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class NewUserRequestNetworkEntity(
    @SerializedName("username")
    var username: String?,
    @SerializedName("mail")
    var mail: String?,
    @SerializedName("password")
    var password: String?
)