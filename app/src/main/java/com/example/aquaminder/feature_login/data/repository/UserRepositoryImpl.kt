package com.example.aquaminder.feature_login.data.repository

import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.data.local.dao.UserDao
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
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val webService: WebService,
    private val sharedPreferences: SharedPreferencesUtil
) : UserRepository {

    override suspend fun loginUser(
        user: LoginUserRequestDomainModel,
    ): LoginUserResponseDomainModel {
        //  TODO GC DELETE MOCK
//        val response = webService.loginUser(user.toNetworkEntity())
        val response = LoginUserResponseNetworkEntity(
            status = 200,
            username = "pepe",
            mail = "asd@mail.com",
            password = "1234"
        )
        delay(2000)
        return response.toDomainModel()
    }


    override suspend fun registerUser(
        user: NewUserRequestDomainModel
    ): NewUserResponseDomainModel {
        // TODO GC DELETE APPDATABASE and DA0
//        userDao.insertUser(user.toEntity())
        return webService.registerUser(user.toNetworkEntity()).toDomainModel()
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

