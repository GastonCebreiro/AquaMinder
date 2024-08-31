package com.example.aquaminder.feature_login.data.repository

import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.data.local.dao.UserDao
import com.example.aquaminder.feature_login.data.local.entity.toDomainModel
import com.example.aquaminder.feature_login.data.remote.model.response.NewPasswordResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.toEntity
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferencesUtil
) : UserRepository {

    override suspend fun getUser(
        name: String,
    ): UserDomainModel? =
        userDao.getUser(name)?.toDomainModel()

    override suspend fun insertUser(
        user: UserDomainModel
    ) {
        userDao.insertUser(user.toEntity())
    }

    override fun saveUserLogged(user: UserDomainModel) {
        sharedPreferences.setUserLoggedName(user.name)
        sharedPreferences.setUserLoggedMail(user.mail)
        sharedPreferences.setUserLoggedPassword(user.password)
    }

    override fun getUserLogged(): UserDomainModel {
        val name = sharedPreferences.getUserLoggedName().orEmpty()
        val mail = sharedPreferences.getUserLoggedMail().orEmpty()
        val password = sharedPreferences.getUserLoggedPassword().orEmpty()
        return UserDomainModel(name, mail, password)
    }

    override fun saveKeepValues(isChecked: Boolean) {
        sharedPreferences.setKeepValues(isChecked)
    }

    override fun getKeepValues(): Boolean {
        return sharedPreferences.getKeepValues()
    }

    override suspend fun askNewPassword(request: NewPasswordRequestDomainModel): NewPasswordResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR NEW PASSWORD
//        val response: NewPasswordResponseNetworkEntity = loginService.askNewPassword(request)
        val response = NewPasswordResponseNetworkEntity()
        return response.toDomainModel()
        //return NewPasswordResponseDomainModel(status = 0, message = "Se envio el mail")
    }
}

