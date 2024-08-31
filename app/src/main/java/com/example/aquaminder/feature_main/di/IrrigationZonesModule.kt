package com.example.aquaminder.feature_main.di

import com.example.aquaminder.core.data.AppDatabase
import com.example.aquaminder.feature_login.data.local.dao.UserDao
import com.example.aquaminder.feature_login.data.repository.UserRepositoryImpl
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_main.data.repository.IrrigationZonesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class IrrigationZonesModule {

    @Singleton
    @Provides
    fun providesIrrigationZonesRepository(irrigationZonesRepositoryImpl: IrrigationZonesRepositoryImpl): IrrigationZonesRepository =
        irrigationZonesRepositoryImpl
}