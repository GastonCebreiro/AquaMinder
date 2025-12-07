package com.example.aquaminder.feature_weather.utils

import android.graphics.Color
import android.webkit.WebView
import com.example.aquaminder.feature_weather.domain.model.IconWeather

object WeatherUtils {

    fun WebView.setWeatherIcon(icon: IconWeather, isSmall: Boolean = false) {
        val small = if (isSmall) "small_" else ""
        setBackgroundColor(Color.TRANSPARENT)
        settings.javaScriptEnabled = true
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        setOnTouchListener { _, _ -> true }
        loadUrl("file:///android_asset/$small${icon.fileName}.html")
    }

    fun codeToIconWeather(code: Int?): IconWeather {
        return when (code) {
            0 -> IconWeather.SUNNY
            1 -> IconWeather.CLOUDY
            2 -> IconWeather.RAINY
            3 -> IconWeather.STORMY
            else -> IconWeather.CLOUDY
        }
    }
}