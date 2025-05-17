package com.example.aquaminder.feature_notifications.data.repository

import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.data.remote.model.response.LoginUserResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.NewPasswordResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.LoginUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.toNetworkEntity
import com.example.aquaminder.feature_login.domain.model.response.LoginUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_notifications.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_notifications.domain.model.response.TokenResponseDomainModel
import com.example.aquaminder.feature_notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferencesUtil
) : NotificationRepository {

    override  fun saveToken(
        token: String
    ) {
        sharedPreferences.setToken(token)
    }

    override  fun getToken(): String {
        return sharedPreferences.getToken()
    }

}

