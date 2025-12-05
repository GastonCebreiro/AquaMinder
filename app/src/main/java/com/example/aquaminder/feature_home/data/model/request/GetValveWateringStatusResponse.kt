package com.example.aquaminder.feature_home.data.model.request

import com.example.aquaminder.feature_home.domain.model.ValveWateringStatusDomainModel
import com.google.gson.annotations.SerializedName

data class GetValveWateringStatusResponse(
    val status: Int? = null,
    @SerializedName("is_watering")
    val isWatering: Int? = null
)

fun GetValveWateringStatusResponse.toDomainModel() = ValveWateringStatusDomainModel(
    isWatering = isWatering == 1
)