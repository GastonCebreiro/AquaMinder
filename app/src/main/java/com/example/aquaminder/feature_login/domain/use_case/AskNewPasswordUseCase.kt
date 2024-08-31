package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.feature_login.data.remote.model.request.NewPasswordRequestNetworkEntity
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_login.utils.NewPasswordEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AskNewPasswordUseCase @Inject constructor(
    private val resources: Resources,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        request: NewPasswordRequestDomainModel
    ): Flow<NewPasswordEvent> = flow {
        try {
            val response = userRepository.askNewPassword(request)
            when (response.status) {
                STATUS_OK -> {
                    emit(NewPasswordEvent.Success(response.message))
                }
                else -> {
                    emit(NewPasswordEvent.UsernameNotFound(resources.getString(R.string.error_msg_new_user_not_found)))
                }
            }

        } catch (e: Exception) {
            val errorMsg = "${resources.getString(R.string.error_msg_unknown)}: ${e.message}"
            emit(NewPasswordEvent.Error(errorMsg))
        }
    }
}