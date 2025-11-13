package com.example.aquaminder.feature_home.data.model

import com.example.aquaminder.feature_configuration.presentation.fragments.LastWatersDomainModel
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.google.firebase.Timestamp
import com.google.gson.annotations.SerializedName

data class LastWatersNetworkEntity(
    @SerializedName("date") // 2025-12-31
    val date: String? = null,
    @SerializedName("time") // 23:59
    val time: String? = null,
    @SerializedName("is_skipped") // 23:59
    val isSkipped: Boolean? = null,
)

fun LastWatersNetworkEntity.toDomainModel() = LastWatersDomainModel(
    date = date.orEmpty(),
    time = time.orEmpty(),
    isSkipped = isSkipped ?: false
)
