package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_login.utils.LoginEvent
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
    ): Flow<LoginEvent> = flow {
        try {
            val user = userRepository.getUser(name)

            when {
                user == null -> {
                    emit(LoginEvent.UsernameNotFound(resources.getString(R.string.error_msg_new_user_not_found)))
                }

                user.password != password -> {
                    emit(LoginEvent.WrongPassword(resources.getString(R.string.error_msg_invalid_password)))
                }

                else -> {
                    emit(
                        LoginEvent.Success(
                            user,
                            resources.getString(R.string.error_msg_login_succesfull)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            val errorMsg = "${resources.getString(R.string.error_msg_unknown)}: ${e.message}"
            emit(LoginEvent.Error(errorMsg))
        }
    }
}