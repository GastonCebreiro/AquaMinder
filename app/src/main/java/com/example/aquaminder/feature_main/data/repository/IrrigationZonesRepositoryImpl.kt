package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_configuration.data.model.request.IrrigationZoneConfigNetworkEntity
import com.example.aquaminder.feature_configuration.data.model.response.SaveIrrigationZoneConfigResponse
import com.example.aquaminder.feature_home.data.model.LastWatersNetworkEntity
import com.example.aquaminder.feature_home.data.model.NextWatersNetworkEntity
import com.example.aquaminder.feature_home.data.model.ScheduleNetworkEntity
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.data.model.request.GetValveWateringStatusRequest
import com.example.aquaminder.feature_home.data.model.request.GetValveWateringStatusResponse
import com.example.aquaminder.feature_home.data.model.request.ManualWateringRequest
import com.example.aquaminder.feature_home.data.model.request.ManualWateringResponse
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
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val sharedPreferences: SharedPreferencesUtil
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(): GetIrrigationZonesResponse {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
        val response: GetIrrigationZonesResponse = webService.getIrrigationZones()
//        delay(1000)
//        val response = GetIrrigationZonesResponse(
//            status = 200,
////            emptyList()
//            irrigationZones = listOf(
//                IrrigationZoneNetworkEntity(
//                    id = "123456",
//                    name = "JARDIN",
//                    logoId = 3,
//                ),
////                IrrigationZoneNetworkEntity(
////                    uuid = "123457",
////                    name = "BALCON HABITACION",
////                    logoId = R.drawable.ic_card_balcony,
////                    colorId = R.color.card_blue_pool
////                ),
////                IrrigationZoneNetworkEntity(
////                    uuid = "123458",
////                    name = "PARQUE DEL FONDO",
////                    logoId = R.drawable.ic_card_park,
////                    colorId = R.color.card_green_water
////                ),
////                IrrigationZoneNetworkEntity(
////                    uuid = "123459",
////                    name = "INVERNADERO",
////                    logoId = R.drawable.ic_card_flowers,
////                    colorId = R.color.card_gray
////                )
//            )
//        )
        return response
    }

    override suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel {
        // TODO GC DELETE MOCK
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
        val response: GetIrrigationZoneDetailsResponse = webService.getIrrigationZoneDetails(request.toMap())
//        delay(1000)
//        val response = GetIrrigationZoneDetailsResponse(
//            status = 200,
//            uuid = "123456",
//            name = "JARDIN",
//            logoId = 3,
//            address = "Av. Juan Bautista Alberdi 1045, C1424 Cdad. Autónoma de Buenos Aires, Argentina",
//            valves = listOf(
//                ValveNetworkEntity(
//                    id = 1,
//                    controlMode = ControlMode.SCHEDULED,
//                    humidityMin = 45,
//                    humidityMax = 80,
//                    isWeatherChecked = 1,
//                    schedule = ScheduleNetworkEntity(
//                        frequencyMode = FrequencyMode.INTERVAL_DAYS,
//                        intervalDays = 5,
//                        waterTimes = listOf(
//                            "10:30",
//                            "15:50",
//                            "22:00"
//                        ),
//                        duration = 20
//                    ),
//                    isActive = 1,
//                    isWatering = 1,
//                    lastWaters = listOf(
//                        LastWatersNetworkEntity(
//                            date = "2025-11-21",
//                            time = "23:40",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-11-21",
//                            time = "10:40",
//                            isSkipped = 1
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-11-20",
//                            time = "12:11",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-10-12",
//                            time = "23:40",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-10-10",
//                            time = "23:40",
//                            isSkipped = 1
//                        )
//                    ),
//                    nextWaters =  listOf(
//                        NextWatersNetworkEntity(
//                            date = "2025-12-05",
//                            time = "16:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-05",
//                            time = "23:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-06",
//                            time = "10:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-06",
//                            time = "23:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-07",
//                            time = "23:40",
//                        ),
//                    ),
//                    lastHumidity = listOf(0,10,20,30,40,50,60,70,80,90,100,90,80,70,60,50,40,30,20,10,0,10,20,30)
//                ),
//                ValveNetworkEntity(
//                    id = 2,
//                    controlMode = ControlMode.SENSOR,
//                    humidityMin = 55,
//                    humidityMax = 75,
//                    schedule = ScheduleNetworkEntity(
//                        duration = 30
//                    ),
//                    isWeatherChecked = 0,
//                    isActive = 0,
//                    lastHumidity = listOf(0,10,20,30,40,50,60,70,80,90,100,90,80,70,60,50,40,30,20,10,0,10,20,30),
//                    lastWaters = listOf(
//                        LastWatersNetworkEntity(
//                            date = "2025-12-31",
//                            time = "23:40",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-10-16",
//                            time = "10:40",
//                            isSkipped = 1
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-11-24",
//                            time = "12:11",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-12-24",
//                            time = "23:40",
//                            isSkipped = 0
//                        ),
//                        LastWatersNetworkEntity(
//                            date = "2025-08-30",
//                            time = "23:40",
//                            isSkipped = 1
//                        )
//                    ),
//                    nextWaters =  listOf(
//                        NextWatersNetworkEntity(
//                            date = "2025-12-05",
//                            time = "16:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-05",
//                            time = "23:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-06",
//                            time = "10:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-06",
//                            time = "23:40",
//                        ),
//                        NextWatersNetworkEntity(
//                            date = "2025-12-07",
//                            time = "23:40",
//                        ),
//                    ),
//                ),
//                ValveNetworkEntity(
//                    id = 3,
//                    controlMode = ControlMode.SCHEDULED,
//                    schedule = ScheduleNetworkEntity(
//                        frequencyMode = FrequencyMode.SELECTED_DAYS,
//                        daysOfWeek = listOf(
//                            "MONDAY",
//                            "TUESDAY",
//                            "SATURDAY"
//                        ),
//                        waterTimes = listOf(
//                            "8:30",
//                            "13:50",
//                            "19:00"
//                        ),
//                        duration = 45
//                    ),
//                    isWeatherChecked = 1,
//                    isActive = 1
//                )
//            )
//        )
        return response
    }

    override suspend fun saveConfig(request: IrrigationZoneConfigNetworkEntity): SaveIrrigationZoneConfigResponse {
        val response: SaveIrrigationZoneConfigResponse = webService.saveValvesConfig(request)
//        // TODO GC DELETE MOCK
//        delay(3000)
//        val response = SaveIrrigationZoneConfigResponse(200)
        return response
    }


    override suspend fun getValveWateringStatus(request: GetValveWateringStatusRequest): GetValveWateringStatusResponse {
        val response: GetValveWateringStatusResponse = webService.getValveWateringStatus(request.toMap())
//        // TODO GC DELETE MOCK
//        delay(300)
//        // Mock muy simple:
//        // Cambiemos el valor para simular que a veces está regando y a veces no
//        val isWatering = (0..1).random()
//        val response = GetValveWateringStatusResponse(
//            status = 200,
//            isWatering = isWatering
//        )
        return response
    }

    override suspend fun manualWatering(request: ManualWateringRequest): ManualWateringResponse {
        val response: ManualWateringResponse = webService.manualWatering(request)
//        // TODO GC DELETE MOCK
//        delay(3000)
//        val response = ManualWateringResponse(200)
        return response
    }
}

