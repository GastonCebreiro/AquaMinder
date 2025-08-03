package com.example.aquaminder.feature_login.data.repository

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.request.LoginUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewUserRequestDomainModel
import com.example.aquaminder.feature_login.domain.model.response.LoginUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewUserResponseDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import java.io.IOException


class FakeUserRepository : UserRepository {

    var userToReturn: UserDomainModel = UserDomainModel("", "", "")
    var statusToReturn: Int = 200
    var shouldThrowIOException: Boolean = false
    var shouldThrowGenericException: Boolean = false

    override suspend fun loginUser(request: LoginUserRequestDomainModel): LoginUserResponseDomainModel {
        if (shouldThrowIOException) throw IOException("Simulated IO Exception")
        if (shouldThrowGenericException) throw Exception("Simulated Exception")

        return LoginUserResponseDomainModel(
            status = statusToReturn,
            user = userToReturn,
            message = ""
        )
    }

    override suspend fun registerUser(user: NewUserRequestDomainModel): NewUserResponseDomainModel {
        TODO("Not yet implemented")
    }

    override fun saveUserLogged(user: UserDomainModel) {
        TODO("Not yet implemented")
    }

    override fun getUserLogged(): UserDomainModel {
        TODO("Not yet implemented")
    }

    override fun saveKeepValues(isChecked: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getKeepValues(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun askNewPassword(request: NewPasswordRequestDomainModel): NewPasswordResponseDomainModel {
        TODO("Not yet implemented")
    }
}