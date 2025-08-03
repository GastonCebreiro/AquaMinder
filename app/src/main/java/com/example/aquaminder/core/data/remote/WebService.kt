package com.example.aquaminder.core.data.remote

import com.example.aquaminder.feature_configuration.data.model.response.GetIrrigationZoneConfigResponse
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponse
import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.request.NewUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.LoginUserResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.NewUserResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponse
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.SaveIrrigationZoneResponseNetworkEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryMap
import javax.inject.Singleton

@Singleton
interface WebService {

    @Headers("Accept: application/json")
    @POST(REGISTER_USER)
    suspend fun registerUser(@Body user: NewUserRequestNetworkEntity): NewUserResponseNetworkEntity

    @POST(LOGIN_USER)
    suspend fun loginUser(@Body user: LoginUserRequestNetworkEntity): LoginUserResponseNetworkEntity

    @GET(GET_IRRIGATION_ZONES)
    suspend fun getIrrigationZones(): GetIrrigationZonesResponse

    @POST(SAVE_IRRIGATION_ZONE)
    suspend fun saveIrrigationZone(@Body request: IrrigationZoneNetworkEntity): SaveIrrigationZoneResponseNetworkEntity

    @GET(GET_IRRIGATION_ZONE_DETAILS)
    suspend fun getIrrigationZoneDetails(@QueryMap request: Map<String, String>): GetIrrigationZoneDetailsResponse

    @GET(GET_IRRIGATION_ZONE_CONFIGURATION)
    suspend fun getIrrigationZoneConfig(@QueryMap request: Map<String, String>): GetIrrigationZoneConfigResponse

    companion object {
        private const val REGISTER_USER = "register"
        private const val LOGIN_USER = "login"
        private const val ASK_NEW_PASSWORD = "asknewpassword"
        private const val GET_IRRIGATION_ZONES = "equipos"
        private const val SAVE_IRRIGATION_ZONE = "addEquipo"
        private const val GET_IRRIGATION_ZONE_DETAILS = "detallesEquipo"
        private const val GET_IRRIGATION_ZONE_CONFIGURATION = "configuracionEquipo"
    }
}