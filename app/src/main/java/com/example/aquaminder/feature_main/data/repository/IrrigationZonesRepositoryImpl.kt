package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_configuration.data.model.request.GetIrrigationZoneConfigRequest
import com.example.aquaminder.feature_configuration.data.model.response.GetIrrigationZoneConfigResponse
import com.example.aquaminder.feature_home.data.model.ScheduleNetworkEntity
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponse
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
                    humidity = 45,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(8, 30), // 08:30
                        intervalHours = 6,
                        durationMinutes = 20
                    ),
                    isActive = true
                ),
                ValveNetworkEntity(
                    id = 2,
                    humidity = 55,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(14, 0), // 14:00
                        intervalHours = 8,
                        durationMinutes = 30
                    ),
                    isActive = false
                ),
                ValveNetworkEntity(
                    id = 3,
                    humidity = 35,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(20, 15), // 20:15
                        intervalHours = 12,
                        durationMinutes = 45
                    ),
                    isActive = true
                )
            )
        )
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
                    humidity = 45,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(8, 30), // 08:30
                        intervalHours = 6,
                        durationMinutes = 20
                    ),
                    isActive = true
                ),
                ValveNetworkEntity(
                    id = 2,
                    humidity = 55,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(14, 0), // 14:00
                        intervalHours = 8,
                        durationMinutes = 30
                    ),
                    isActive = false
                ),
                ValveNetworkEntity(
                    id = 3,
                    humidity = 35,
                    schedule = ScheduleNetworkEntity(
                        startHour = LocalTime.of(20, 15), // 20:15
                        intervalHours = 12,
                        durationMinutes = 45
                    ),
                    isActive = true
                )
            )
        )
        return response
    }

}

