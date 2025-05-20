package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.gson.annotations.SerializedName

data class ValveNetworkEntity(
    @SerializedName("id_valvula")
    val id: Int? = null,
    @SerializedName("humedad_deseada")
    val selectedHumidity: Int? = null
)

fun ValveNetworkEntity.toDomainModel() = ValveDomainModel(
    selectedHumidity = selectedHumidity ?: 0,
    id = id ?: -1
)
