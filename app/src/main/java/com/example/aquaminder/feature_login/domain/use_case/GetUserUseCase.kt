package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.LoginUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.response.LoginUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val resources: Resources,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        password: String
    ): Flow<ResultEvent<UserDomainModel>> = flow {
        try {
            val userRequest = LoginUserRequestDomainModel(
                name = name,
                password = password
            )
            val response = userRepository.loginUser(userRequest)
            // TODO GC GET ERROR CASES
            when (response.status) {
                STATUS_OK -> {
                    when {
                        response.user.name.isBlank() || response.user.mail.isBlank() || response.user.password.isBlank() -> {
                            emit(ResultEvent.Error(AppError.UsernameNotFound))
                        }
                        else -> {
                            emit(ResultEvent.Success(response.user))
                        }
                    }
                }

//                    emit(ResultEvent.Error(AppError.WrongPassword))
            }
        } catch (e: Exception) {
            emit(ResultEvent.Error(AppError.GenericError(e.message.orEmpty())))
        }
    }

    companion object {
    }
}