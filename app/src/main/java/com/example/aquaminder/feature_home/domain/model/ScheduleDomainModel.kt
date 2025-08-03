package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
data class ScheduleDomainModel(
    val startTime: LocalTime,
    val intervalHours: Int,
    val duration: Int,
): Parcelable
