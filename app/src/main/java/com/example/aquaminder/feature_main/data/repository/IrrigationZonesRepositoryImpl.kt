package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_configuration.data.model.request.GetIrrigationZoneConfigRequest
import com.example.aquaminder.feature_configuration.data.model.request.IrrigationZoneConfigNetworkEntity
import com.example.aquaminder.feature_configuration.data.model.response.GetIrrigationZoneConfigResponse
import com.example.aquaminder.feature_configuration.data.model.response.SaveIrrigationZoneConfigResponse
import com.example.aquaminder.feature_configuration.presentation.fragments.LastWatersDomainModel
import com.example.aquaminder.feature_home.data.model.LastWatersNetworkEntity
import com.example.aquaminder.feature_home.data.model.ScheduleNetworkEntity
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponse
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponse
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.domain.model.toNetworkEntity
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.SaveIrrigationZoneResponseNetworkEntity
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel
import kotlinx.coroutines.delay
import java.time.LocalTime
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val sharedPreferences: SharedPreferencesUtil
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(): GetIrrigationZonesResponse {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: GetIrrigationZonesResponse = webService.getIrrigationZones()
//        delay(1000)
        val response = GetIrrigationZonesResponse(
            status = 200,
//            emptyList()
            irrigationZones = listOf(
                IrrigationZoneNetworkEntity(
                    id = "123456",
                    name = "JARDIN",
                    logoId = R.drawable.ic_card_house,
                ),
//                IrrigationZoneNetworkEntity(
//                    uuid = "123457",
//                    name = "BALCON HABITACION",
//                    logoId = R.drawable.ic_card_balcony,
//                    colorId = R.color.card_blue_pool
//                ),
//                IrrigationZoneNetworkEntity(
//                    uuid = "123458",
//                    name = "PARQUE DEL FONDO",
//                    logoId = R.drawable.ic_card_park,
//                    colorId = R.color.card_green_water
//                ),
//                IrrigationZoneNetworkEntity(
//                    uuid = "123459",
//                    name = "INVERNADERO",
//                    logoId = R.drawable.ic_card_flowers,
//                    colorId = R.color.card_gray
//                )
            )
        )
        return response
    }

    override suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
        val response: SaveIrrigationZoneResponseNetworkEntity =
            webService.saveIrrigationZone(request.toNetworkEntity())
//        val response = SaveIrrigationZoneResponseNetworkEntity(status = 200)
//        delay(1000)
        return response.toDomainModel()
    }

    override fun saveIrrigationZoneIdSelected(id: String) {
        sharedPreferences.setIrrigationZoneIdSelected(id)
    }

    override fun getIrrigationZoneIdSelected(): String {
        return sharedPreferences.getIrrigationZoneIdSelected()
    }

    override suspend fun getIrrigationZoneDetails(request: GetIrrigationZoneDetailsRequest): GetIrrigationZoneDetailsResponse {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONE DETAILS
//        val response: GetIrrigationZoneDetailsResponse = webService.getIrrigationZoneDetails(request.toMap())
        delay(1000)
        val response = GetIrrigationZoneDetailsResponse(
            status = 200,
            uuid = "123456",
            name = "JARDIN",
            logoId = R.drawable.ic_card_house,
            address = "Av. Juan Bautista Alberdi 1045, C1424 Cdad. Autónoma de Buenos Aires, Argentina",
            valves = listOf(
                ValveNetworkEntity(
                    id = 1,
                    controlMode = ControlMode.SCHEDULED,
                    humidityMin = 45,
                    humidityMax = 80,
                    isWeatherChecked = true,
                    schedule = ScheduleNetworkEntity(
                        frequencyMode = FrequencyMode.INTERVAL_DAYS,
                        intervalDays = 5,
                        waterTimes = listOf(
                            "10:30",
                            "15:50",
                            "22:00"
                        ),
                        duration = 20
                    ),
                    isActive = true,
                    isWatering = true,
                    lastWaters = listOf(
                        LastWatersNetworkEntity(
                            date = "2025-11-21",
                            time = "23:40",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-11-21",
                            time = "10:40",
                            isSkipped = true
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-11-20",
                            time = "12:11",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-10-12",
                            time = "23:40",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-10-10",
                            time = "23:40",
                            isSkipped = true
                        )
                    ),
                    lastHumidity = listOf(0,10,20,30,40,50,60,70,80,90,100,90,80,70,60,50,40,30,20,10,0,10,20,30)
                ),
                ValveNetworkEntity(
                    id = 2,
                    controlMode = ControlMode.SENSOR,
                    humidityMin = 55,
                    humidityMax = 75,
                    schedule = ScheduleNetworkEntity(
                        duration = 30
                    ),
                    isWeatherChecked = false,
                    isActive = false,
                    lastHumidity = listOf(0,10,20,30,40,50,60,70,80,90,100,90,80,70,60,50,40,30,20,10,0,10,20,30),
                    lastWaters = listOf(
                        LastWatersNetworkEntity(
                            date = "2025-12-31",
                            time = "23:40",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-10-16",
                            time = "10:40",
                            isSkipped = true
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-11-24",
                            time = "12:11",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-12-24",
                            time = "23:40",
                            isSkipped = false
                        ),
                        LastWatersNetworkEntity(
                            date = "2025-08-30",
                            time = "23:40",
                            isSkipped = true
                        )
                    ),
                ),
                ValveNetworkEntity(
                    id = 3,
                    controlMode = ControlMode.SCHEDULED,
                    schedule = ScheduleNetworkEntity(
                        frequencyMode = FrequencyMode.SELECTED_DAYS,
                        daysOfWeek = listOf(
                            "MONDAY",
                            "TUESDAY",
                            "SATURDAY"
                        ),
                        waterTimes = listOf(
                            "8:30",
                            "13:50",
                            "19:00"
                        ),
                        duration = 45
                    ),
                    isWeatherChecked = true,
                    isActive = true
                )
            )
        )
        return response
    }

    override suspend fun saveConfig(request: IrrigationZoneConfigNetworkEntity): SaveIrrigationZoneConfigResponse {
//        val response: SaveIrrigationZoneConfigResponse = webService.saveValvesConfig(request)
        // TODO GC DELETE MOCK
        delay(3000)
        val response = SaveIrrigationZoneConfigResponse(200)
        return response
    }


    override suspend fun getIrrigationZoneConfig(request: GetIrrigationZoneConfigRequest): GetIrrigationZoneConfigResponse {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONE Config
//        val response: GetIrrigationZoneConfigResponse = webService.getIrrigationZoneConfig(request.toMap())
        delay(1000)
        val response = GetIrrigationZoneConfigResponse(
            status = 200,
            uuid = "123456",
            valves = listOf(
                ValveNetworkEntity(
                    id = 1,
                    controlMode = ControlMode.SCHEDULED,
                    humidityMin = 45,
                    humidityMax = 80,
                    isWeatherChecked = true,
                    schedule = ScheduleNetworkEntity(
                        frequencyMode = FrequencyMode.INTERVAL_DAYS,
                        intervalDays = 5,
                        waterTimes = listOf(
                            "10:30",
                            "15:50",
                            "22:00"
                        ),
                        duration = 20
                    ),
                    isActive = true,
                    isWatering = true,
                    lastWaters = listOf(
                        LastWatersNetworkEntity(
                            date = "2025-12-31",
                            time = "23:40",
                            isSkipped = false
                        )
                    ),
                    lastHumidity = listOf(24,22,20,40,60)
                ),
                ValveNetworkEntity(
                    id = 2,
                    controlMode = ControlMode.SENSOR,
                    humidityMin = 55,
                    humidityMax = 75,
                    schedule = ScheduleNetworkEntity(
                        duration = 30
                    ),
                    isWeatherChecked = false,
                    isActive = false
                ),
                ValveNetworkEntity(
                    id = 3,
                    controlMode = ControlMode.SCHEDULED,
                    schedule = ScheduleNetworkEntity(
                        frequencyMode = FrequencyMode.SELECTED_DAYS,
                        daysOfWeek = listOf(
                            "MONDAY",
                            "TUESDAY",
                            "SATURDAY"
                        ),
                        waterTimes = listOf(
                            "8:30",
                            "13:50",
                            "19:00"
                        ),
                        duration = 45
                    ),
                    isWeatherChecked = true,
                    isActive = true
                )
            ),
            isCheckHumidityEnabled = true
        )
        return response
    }

}

