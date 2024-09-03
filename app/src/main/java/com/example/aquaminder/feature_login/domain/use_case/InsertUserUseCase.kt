package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userToInsert: UserDomainModel
    ): Flow<ResultEvent<UserDomainModel>> = flow {
        try {
            val userRequest = NewUserRequestDomainModel(
                name = userToInsert.name,
                mail = userToInsert.mail,
                password = userToInsert.password
            )
            val response = userRepository.registerUser(userRequest)
            when (response.status) {
                STATUS_OK -> {
                    emit(ResultEvent.Success(userToInsert))
                }
                MAIL_UNAVAILABLE -> {
                    emit(ResultEvent.Error(AppError.MailUnavailable))
                }
                USERNAME_UNAVAILABLE -> {
                    emit(ResultEvent.Error(AppError.UsernameUnavailable))
                }
                else -> {
                    emit(ResultEvent.Error(AppError.GenericError()))
                }
            }
        } catch (e: IOException) {
            emit(ResultEvent.Error(AppError.NetworkError))
        } catch (e: Exception) {
            emit(ResultEvent.Error(AppError.GenericError(e.message.orEmpty())))
        }
    }

    companion object {
        private const val USERNAME_UNAVAILABLE = 4
        private const val MAIL_UNAVAILABLE = 3
    }
}