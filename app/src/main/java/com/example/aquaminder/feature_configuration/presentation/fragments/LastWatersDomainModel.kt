package com.example.aquaminder.feature_configuration.presentation.fragments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastWatersDomainModel(
    val date: String,
    val time: String,
    val isSkipped: Boolean,
): Parcelable