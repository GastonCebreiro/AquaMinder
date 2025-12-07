package com.example.aquaminder.core.data.remote

import com.example.aquaminder.feature_configuration.data.model.request.IrrigationZoneConfigNetworkEntity
import com.example.aquaminder.feature_configuration.data.model.response.GetIrrigationZoneConfigResponse
import com.example.aquaminder.feature_configuration.data.model.response.SaveIrrigationZoneConfigResponse
import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_home.data.model.request.GetValveWateringStatusResponse
import com.example.aquaminder.feature_home.data.model.request.ManualWateringRequest
import com.example.aquaminder.feature_home.data.model.request.ManualWateringResponse
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponse
import com.example.aquaminder.feature_login.data.remote.model.request.LoginUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.request.NewUserRequestNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.LoginUserResponseNetworkEntity
import com.example.aquaminder.feature_login.data.remote.model.response.NewUserResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponse
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.SaveIrrigationZoneResponseNetworkEntity
import com.example.aquaminder.feature_weather.data.model.response.GetWeatherResponse
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

    @GET(GET_VALVE_WATERING_STATUS)
    suspend fun getValveWateringStatus(
        @QueryMap request: Map<String, String>
    ): GetValveWateringStatusResponse

    @POST(SAVE_VALVES_CONFIG)
    suspend fun saveValvesConfig(@Body request: IrrigationZoneConfigNetworkEntity): SaveIrrigationZoneConfigResponse

    @GET(GET_WEATHER)
    suspend fun getWeather(@QueryMap request: Map<String, String>): GetWeatherResponse

    @POST(MANUAL_WATERING)
    suspend fun manualWatering(@Body request: ManualWateringRequest): ManualWateringResponse


    companion object {
        private const val REGISTER_USER = "register"
        private const val LOGIN_USER = "login"
        private const val ASK_NEW_PASSWORD = "asknewpassword"
        private const val GET_IRRIGATION_ZONES = "equipos"
        private const val SAVE_IRRIGATION_ZONE = "addEquipo"
        private const val GET_IRRIGATION_ZONE_DETAILS = "detallesEquipo"
        private const val GET_IRRIGATION_ZONE_CONFIGURATION = "configuracionEquipo"
        private const val SAVE_VALVES_CONFIG = "guardarConfig"
        private const val GET_VALVE_WATERING_STATUS = "wateringStatus"
        private const val GET_WEATHER = "weather"
        private const val MANUAL_WATERING = "manualWatering"
    }
}