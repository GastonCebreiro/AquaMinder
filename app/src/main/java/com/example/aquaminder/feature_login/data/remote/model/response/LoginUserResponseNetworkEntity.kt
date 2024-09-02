package com.example.aquaminder.feature_login.data.remote.model.response

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.response.LoginUserResponseDomainModel

data class LoginUserResponseNetworkEntity(
    val status: Int? = null,
    val message: String? = null,
    val username: String? = null,
    val mail: String? = null,
    val password: String? = null
)

fun LoginUserResponseNetworkEntity.toDomainModel() = LoginUserResponseDomainModel(
        status = status ?: -1,
        message = message.orEmpty(),
        user = UserDomainModel(
            username.orEmpty(),
            mail.orEmpty(),
            password.orEmpty()
        )
)