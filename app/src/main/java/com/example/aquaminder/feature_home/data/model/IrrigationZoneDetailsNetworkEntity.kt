package com.example.aquaminder.feature_home.data.model

import com.google.gson.annotations.SerializedName

data class IrrigationZoneDetailsNetworkEntity(
    @SerializedName("id")
    val uuid: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("logoId")
    var logoId: Int? = null,
    @SerializedName("colorId")
    var colorId: Int? = null,
    // valveQuantity
    // isSenseHumidity
    // isCheckWeather
    // isEnabled
    // TODO GC CREATE THIS
//    var valve: ValveNetworkEntity
)
