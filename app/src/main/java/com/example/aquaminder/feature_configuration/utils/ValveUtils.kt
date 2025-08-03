package com.example.aquaminder.feature_configuration.utils

import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import java.time.LocalTime

object ValveUtils {

    fun getHumidityDescription(humidity: Int): String = "$humidity %"

    fun getStartTimeDescription(time: LocalTime?): String {
        val hour = time?.hour ?: 0
        val minute = time?.minute ?: 0
        val startTime = String.format("%02d:%02d", hour, minute)
        return "$startTime hs"
    }

    fun getIntervalHoursDescription(intervalHours: Int?): String =
        intervalHours?.let {
            "$intervalHours " + if (intervalHours > 1) "horas" else "hora"
        } ?: run { "" }

    fun getDurationDescription(duration: Int?): String =
        duration?.let {
            "$duration " + if (duration > 1) "minutos" else "minuto"
        } ?: run { "" }
}

class InputFilterMinMax(private val min: Int, private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val newVal = (dest.toString() + source.toString()).toInt()
            if (newVal in min..max) {
                return null
            }
        } catch (e: NumberFormatException) {
            Log.e("InputFilterMinMax", "ERROR: $e")
        }
        return ""
    }
}
