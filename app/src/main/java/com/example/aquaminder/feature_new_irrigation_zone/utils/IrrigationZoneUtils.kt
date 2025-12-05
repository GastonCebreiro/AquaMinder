package com.example.aquaminder.feature_new_irrigation_zone.utils

import com.example.aquaminder.R

object IrrigationZoneUtils {

    fun getLogos(): List<Int> = listOf(
        R.drawable.ic_card_house,
        R.drawable.ic_card_balcony,
        R.drawable.ic_card_flowers,
        R.drawable.ic_card_park
    )

    fun getColors(): List<Int> = listOf(
        R.color.card_light_blue,
        R.color.card_blue_pool,
        R.color.card_green_water,
        R.color.card_gray
    )

    fun getLogoById(logoId: Int): Int {
        return try {
            getLogos()[logoId]
        } catch(e: Exception) {
            getLogos()[0]
        }
    }

    fun getColorById(logoId: Int): Int {
        return try {
            getColors()[logoId]
        } catch(e: Exception) {
            getColors()[0]
        }
    }
}