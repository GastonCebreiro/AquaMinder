package com.example.aquaminder.core.data.remote

import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.request.NewUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.LoginUserResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.NewUserResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.request.GetIrrigationZonesRequestNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponseNetworkEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface WebService {

    @Headers("Accept: application/json")
    @POST(REGISTER_USER)
    suspend fun registerUser(@Body user: NewUserRequestNetworkEntity): NewUserResponseNetworkEntity

    @POST(LOGIN_USER)
    suspend fun loginUser(@Body user: LoginUserRequestNetworkEntity): LoginUserResponseNetworkEntity

    @GET(GET_IRRIGATION_ZONES)
    suspend fun getIrrigationZones(@Body request: GetIrrigationZonesRequestNetworkEntity): GetIrrigationZonesResponseNetworkEntity

    companion object {
        private const val REGISTER_USER = "register"
        private const val LOGIN_USER = "loginuser"
        private const val ASK_NEW_PASSWORD = "asknewpassword"
        private const val GET_IRRIGATION_ZONES = "getirrigationzones"
    }
}