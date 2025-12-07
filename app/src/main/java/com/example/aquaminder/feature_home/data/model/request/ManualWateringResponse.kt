package com.example.aquaminder.feature_home.data.model.request

import com.example.aquaminder.feature_home.domain.model.ValveWateringStatusDomainModel
import com.google.gson.annotations.SerializedName

data class ManualWateringResponse(
    val status: Int? = null,
)