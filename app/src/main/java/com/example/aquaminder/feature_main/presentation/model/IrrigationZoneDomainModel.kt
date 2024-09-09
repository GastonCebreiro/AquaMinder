package com.example.aquaminder.feature_main.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var colorId: Int
): Parcelable