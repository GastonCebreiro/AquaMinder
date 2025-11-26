package com.example.aquaminder.feature_weather.di

import com.example.aquaminder.feature_main.data.repository.IrrigationZonesRepositoryImpl
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.aquaminder.feature_weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Singleton
    @Provides
    fun providesWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository =
        weatherRepositoryImpl
}