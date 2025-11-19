package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_home.data.model.ScheduleNetworkEntity
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class ScheduleDomainModel(
    val frequencyMode: FrequencyMode,
    val intervalDays: Int,
    val daysOfWeek: List<DayOfWeek>,
    val waterTimes: List<LocalTime>,
    val duration: Int,
): Parcelable

fun ScheduleDomainModel.toNetworkEntity() = ScheduleNetworkEntity(
    frequencyMode = frequencyMode,
    intervalDays = intervalDays,
    daysOfWeek = daysOfWeek.map { it.name },
    waterTimes = waterTimes.map { it.toString() }, // LocalTime → "HH:mm"
    duration = duration
)