package com.example.aquaminder.feature_configuration.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.aquaminder.feature_home.data.model.NextWatersNetworkEntity
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
data class NextWatersDomainModel(
    val date: String,
    val time: String,
): Parcelable

fun NextWatersDomainModel.toNetworkEntity() = NextWatersNetworkEntity(
    date = date,
    time = time,
)

@SuppressLint("NewApi")
fun NextWatersDomainModel.getFormattedDate(): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateParsed = LocalDate.parse(this.date, formatter)

        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        when (dateParsed) {
            today -> "Hoy"
            tomorrow -> "Mañana"
            else -> dateParsed.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

    } catch (e: Exception) {
        this.date
    }
}
