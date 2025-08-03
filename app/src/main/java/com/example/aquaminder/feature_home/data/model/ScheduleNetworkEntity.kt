package com.example.aquaminder.feature_home.data.model

import android.os.Parcelable
import com.example.aquaminder.feature_home.domain.model.ScheduleDomainModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
data class ScheduleNetworkEntity(
    @SerializedName("hora_inicio")
    val startHour: LocalTime?,
    @SerializedName("intervalo")
    val intervalHours: Int?,
    @SerializedName("duracion")
    val durationMinutes: Int?,
): Parcelable


fun ScheduleNetworkEntity.toDomainModel() = ScheduleDomainModel(
    startTime = startHour ?: LocalTime.of(0,0,0),
    intervalHours = intervalHours ?: 0,
    duration = durationMinutes ?: 0

)