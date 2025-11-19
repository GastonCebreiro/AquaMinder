package com.example.aquaminder.feature_configuration.presentation.fragments

import android.os.Parcelable
import com.example.aquaminder.feature_home.data.model.LastWatersNetworkEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastWatersDomainModel(
    val date: String,
    val time: String,
    val isSkipped: Boolean,
): Parcelable

fun LastWatersDomainModel.toNetworkEntity() = LastWatersNetworkEntity(
    date = date,
    time = time,
    isSkipped = isSkipped
)