package com.example.aquaminder.core.utils

import com.example.aquaminder.feature_login.domain.use_case.GetUsernameUseCase
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class UsernameInterceptor @Inject constructor(
    private val getUsernameUseCaseProvider: Provider<GetUsernameUseCase>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val username = getUsernameUseCaseProvider.get().invoke()
        val request = chain.request().newBuilder()
            .addHeader("Username", username ?: "")
            .build()

        return chain.proceed(request)
    }
}