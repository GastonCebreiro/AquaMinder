package com.example.aquaminder.feature_main.presentation.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneUiModel(
    var uuid: String,
    var name: String,
    @DrawableRes var logoId: Int,
    @ColorRes var colorId: Int
): Parcelable
