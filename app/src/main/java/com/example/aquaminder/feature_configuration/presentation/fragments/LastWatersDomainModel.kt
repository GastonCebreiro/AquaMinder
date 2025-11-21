package com.example.aquaminder.feature_configuration.presentation.fragments

import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.aquaminder.feature_home.data.model.LastWatersNetworkEntity
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

@SuppressLint("NewApi")
fun LastWatersDomainModel.getFormattedDate(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateParsed = LocalDate.parse(this.date, formatter)

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        when (dateParsed) {
            today -> "Hoy"
            yesterday -> "Ayer"
            else -> dateParsed.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

    } catch (e: Exception) {
        this.date
    }
}
