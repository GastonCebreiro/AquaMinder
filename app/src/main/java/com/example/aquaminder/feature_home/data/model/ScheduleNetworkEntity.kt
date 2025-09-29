package com.example.aquaminder.feature_home.data.model

import android.os.Parcelable
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_home.domain.model.ScheduleDomainModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalTime

@Parcelize
data class ScheduleNetworkEntity(
    @SerializedName("modoFrecuencia")
    val frequencyMode: FrequencyMode? = null,
    @SerializedName("intervaloDias")
    val intervalDays: Int? = null,
    @SerializedName("diasSeleccionados") // MONDAY TUESDAY WEDNESDAY THURSDAY FRIDAY SATURDAY SUNDAY
    val daysOfWeek: List<String>? = null,
    @SerializedName("horasDeRiego") // "06:30" "12:00" "18:45"
    val waterTimes: List<String>? = null,
    @SerializedName("duracion")
    val duration: Int? = null,
): Parcelable


fun ScheduleNetworkEntity.toDomainModel() = ScheduleDomainModel(
    frequencyMode = frequencyMode ?: FrequencyMode.INTERVAL_DAYS,
    intervalDays = intervalDays ?: 1,
    daysOfWeek = daysOfWeek?.mapNotNull { runCatching { DayOfWeek.valueOf(it) }.getOrNull() }
        ?: emptyList(),
    waterTimes = waterTimes?.mapNotNull { runCatching { LocalTime.parse(it) }.getOrNull() }
        ?: emptyList(),
    duration = duration ?: 0
)