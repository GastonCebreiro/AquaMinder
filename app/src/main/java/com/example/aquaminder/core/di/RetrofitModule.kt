package com.example.aquaminder.core.di

import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.AppConstants.BASE_URL
import com.example.aquaminder.core.utils.UsernameInterceptor
import com.example.aquaminder.feature_login.domain.use_case.GetUserLoggedUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetUsernameUseCase
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
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun providesGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideUsernameInterceptor(
        getUsernameUseCaseProvider: Provider<GetUsernameUseCase>
    ): UsernameInterceptor {
        return UsernameInterceptor(getUsernameUseCaseProvider)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        usernameInterceptor: UsernameInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(usernameInterceptor)
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
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): WebService =
        retrofit.create(WebService::class.java)

}