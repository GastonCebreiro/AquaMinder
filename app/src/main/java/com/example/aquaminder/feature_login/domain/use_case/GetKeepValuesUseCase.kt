package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class GetKeepValuesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Boolean {
        return userRepository.getKeepValues()
    }
}