package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.LoginUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        password: String,
        token: String
    ): ResultEvent<UserDomainModel> {
        return try {
            val userRequest = LoginUserRequestDomainModel(
                name = name,
                password = password,
                token = token
            )
            val response = userRepository.loginUser(userRequest)
            when (response.status) {
                STATUS_OK -> {
                    when {
                        response.user.name.isBlank() || response.user.mail.isBlank() || response.user.password.isBlank() -> {
                            ResultEvent.Error(AppError.UsernameNotFound)
                        }
                        else -> {
                            ResultEvent.Success(response.user)
                        }
                    }
                }
                USERNAME_NOT_FOUND -> {
                    ResultEvent.Error(AppError.UsernameNotFound)
                }
                WRONG_PASSWORD -> {
                    ResultEvent.Error(AppError.WrongPassword)
                }
                else -> {
                    ResultEvent.Error(AppError.GenericError())
                }
            }
        } catch (e: IOException) {
            ResultEvent.Error(AppError.NetworkError)
        } catch (e: Exception) {
            ResultEvent.Error(AppError.GenericError(e.message.orEmpty()))
        }
    }

    companion object {
        private const val USERNAME_NOT_FOUND = 5
        private const val WRONG_PASSWORD = 6
    }
}