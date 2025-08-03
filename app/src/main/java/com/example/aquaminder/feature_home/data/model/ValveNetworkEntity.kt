package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.gson.annotations.SerializedName

data class ValveNetworkEntity(
    @SerializedName("id_valvula")
    val id: Int? = null,
    @SerializedName("humedad_deseada")
    val humidity: Int? = null,
    @SerializedName("schedule")
    val schedule: ScheduleNetworkEntity? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null

)

fun ValveNetworkEntity.toDomainModel() = ValveDomainModel(
    humidity = humidity ?: 0,
    id = id ?: -1,
    schedule = schedule?.toDomainModel(),
    isActive = isActive ?: false
)
