package com.example.aquaminder.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun providesGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(65, TimeUnit.SECONDS)
            .writeTimeout(65, TimeUnit.SECONDS)
            .readTimeout(65, TimeUnit.SECONDS)
            .callTimeout(65, TimeUnit.SECONDS)
            .addInterceptor(logging)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientLogging(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://neutrinoapi-qr-code.p.rapidapi.com/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        }

//    @Singleton
//    @Provides
//    fun provideApiService(retrofit: Retrofit): QRCodeApi =
//        retrofit.create(QRCodeApi::class.java)

}