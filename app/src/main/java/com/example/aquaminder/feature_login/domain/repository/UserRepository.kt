package com.example.aquaminder.feature_login.domain.repository

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel


interface UserRepository {

    suspend fun getUser(name: String): UserDomainModel?
    suspend fun insertUser(user: UserDomainModel)
    fun saveUserLogged(user: UserDomainModel)
    fun getUserLogged(): UserDomainModel
    fun saveKeepValues(isChecked: Boolean)
    fun getKeepValues(): Boolean
    suspend fun askNewPassword(request: NewPasswordRequestDomainModel): NewPasswordResponseDomainModel
}