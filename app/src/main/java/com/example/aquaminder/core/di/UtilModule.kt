package com.example.aquaminder.core.di

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import com.example.aquaminder.core.data.AppDatabase
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {

    @Singleton
    @Provides
    fun providesResources(@ApplicationContext context: Context): Resources = context.resources

    @Singleton
    @Provides
    fun providesDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "myDB"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesSharedPreferencesUtil(@ApplicationContext context: Context): SharedPreferencesUtil =
        SharedPreferencesUtil(context)
}