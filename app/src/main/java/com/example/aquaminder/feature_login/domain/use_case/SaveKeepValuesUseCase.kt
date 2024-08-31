package com.example.aquaminder.feature_login.domain.use_case

import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class SaveKeepValuesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        isChecked: Boolean
    ) {
        userRepository.saveKeepValues(isChecked)
    }
}