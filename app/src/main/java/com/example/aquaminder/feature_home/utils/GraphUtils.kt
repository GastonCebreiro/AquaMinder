package com.example.aquaminder.feature_home.utils

import android.annotation.SuppressLint
import java.time.LocalTime

object GraphUtils {

    @SuppressLint("NewApi")
    fun getLast24HoursLabels(): List<String> {
        val now = LocalTime.now().withMinute(0).withSecond(0).withNano(0)

        return (0 until 24)
            .map { now.minusHours(it.toLong()) }
            .reversed()
            .map { time -> "%02d:%02d".format(time.hour, time.minute) }
    }

}