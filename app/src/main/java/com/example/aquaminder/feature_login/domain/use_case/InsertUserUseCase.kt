package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_login.utils.InsertUserEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val resources: Resources,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userToInsert: UserDomainModel
    ): Flow<InsertUserEvent> = flow {
        try {
            userRepository.insertUser(userToInsert)
            emit(InsertUserEvent.Success(userToInsert))
        } catch (e: Exception) {
            val errorMsg = "${resources.getString(R.string.error_msg_unknown)}: ${e.message}"
            emit(InsertUserEvent.Error(errorMsg))
        }
    }
}