package com.example.aquaminder.feature_login.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class GetUserLoggedUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): ResultEvent<UserDomainModel> {
        val user = userRepository.getUserLogged()
        return if (user.name.isBlank() || user.mail.isBlank() || user.mail.isBlank()) {
            ResultEvent.Error(AppError.InvalidUser)
        } else {
            ResultEvent.Success(user)
        }
    }
}