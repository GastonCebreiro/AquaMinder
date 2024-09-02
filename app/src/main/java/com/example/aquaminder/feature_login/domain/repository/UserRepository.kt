package com.example.aquaminder.feature_login.domain.repository

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.LoginUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.response.LoginUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewUserResponseDomainModel


interface UserRepository {

    suspend fun loginUser(user: LoginUserRequestDomainModel): LoginUserResponseDomainModel
    suspend fun registerUser(user: NewUserRequestDomainModel): NewUserResponseDomainModel
    fun saveUserLogged(user: UserDomainModel)
    fun getUserLogged(): UserDomainModel
    fun saveKeepValues(isChecked: Boolean)
    fun getKeepValues(): Boolean
    suspend fun askNewPassword(request: NewPasswordRequestDomainModel): NewPasswordResponseDomainModel
}