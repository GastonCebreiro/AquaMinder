package com.example.aquaminder.feature_home.data.model.request

data class GetValveWateringStatusResponse(
    val status: Int,
    val isWatering: Boolean
)
