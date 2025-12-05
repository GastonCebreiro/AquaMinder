package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_configuration.domain.model.NextWatersDomainModel
import com.google.gson.annotations.SerializedName

data class NextWatersNetworkEntity(
    @SerializedName("date") // 2025-12-31
    val date: String? = null,
    @SerializedName("time") // 23:59
    val time: String? = null,
)

fun NextWatersNetworkEntity.toDomainModel() = NextWatersDomainModel(
    date = date.orEmpty(),
    time = time.orEmpty(),
)
