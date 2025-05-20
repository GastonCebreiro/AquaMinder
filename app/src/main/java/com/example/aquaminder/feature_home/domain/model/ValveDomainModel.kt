package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValveDomainModel(
    val id: Int,
    val selectedHumidity: Int,
): Parcelable
