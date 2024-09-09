package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AskNewPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        request: NewPasswordRequestDomainModel
    ): Flow<ResultEvent<String>> = flow {
        try {
            val response = userRepository.askNewPassword(request)
            when (response.status) {
                STATUS_OK -> {
                    emit(ResultEvent.Success(response.message))
                }

                else -> {
                    emit(
                        ResultEvent.Error(AppError.UsernameNotFound)
                    )
                }
            }

        } catch (e: Exception) {
            emit(ResultEvent.Error(AppError.GenericError(e.message.orEmpty())))
        }
    }
}