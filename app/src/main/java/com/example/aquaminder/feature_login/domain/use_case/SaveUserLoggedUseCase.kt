package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserLoggedUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        user: UserDomainModel
    ) {
        userRepository.saveUserLogged(user)
    }
}