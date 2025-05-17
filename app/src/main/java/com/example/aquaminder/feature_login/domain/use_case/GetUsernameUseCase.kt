package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): String? {
        val user = userRepository.getUserLogged()
        return if (user.name.isBlank() || user.mail.isBlank()) null else user.name
    }
}