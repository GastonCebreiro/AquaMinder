package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_home.data.model.IrrigationZoneDetailsNetworkEntity
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponseNetworkEntity
import com.example.aquaminder.feature_home.data.model.response.toDomainModel
import com.example.aquaminder.feature_home.domain.model.request.GetIrrigationZoneDetailsRequestDomainModel
import com.example.aquaminder.feature_home.domain.model.response.GetIrrigationZoneDetailsResponseDomainModel
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.SaveIrrigationZoneResponseNetworkEntity
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val sharedPreferences: SharedPreferencesUtil
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: GetIrrigationZonesResponseNetworkEntity = webService.getIrrigationZones(request.toNetworkEntity())
        delay(2000)
        val response = GetIrrigationZonesResponseNetworkEntity(
            status = 200,
//            emptyList()
            irrigationZones = listOf(
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "JARDIN",
                    logoId = R.drawable.ic_card_house,
                    colorId = R.color.card_light_blue
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "BALCON HABITACION",
                    logoId = R.drawable.ic_card_balcony,
                    colorId = R.color.card_blue_pool
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "PARQUE DEL FONDO",
                    logoId = R.drawable.ic_card_park,
                    colorId = R.color.card_green_water
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "INVERNADERO",
                    logoId = R.drawable.ic_card_flowers,
                    colorId = R.color.card_gray
                )
            )
        )
        return response.toDomainModel()
    }

    override suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: SaveIrrigationZoneResponseNetworkEntity = webService.saveIrrigationZone(request.toNetworkEntity())
        val response = SaveIrrigationZoneResponseNetworkEntity(status = 200)
        delay(2000)
        return response.toDomainModel()
    }

    override fun saveIrrigationZoneIdSelected(id: String) {
        sharedPreferences.setIrrigationZoneIdSelected(id)
    }

    override fun getIrrigationZoneIdSelected(): String {
        return sharedPreferences.getIrrigationZoneIdSelected()
    }

    override suspend fun getIrrigationZoneDetails(request: GetIrrigationZoneDetailsRequestDomainModel): GetIrrigationZoneDetailsResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONE DETAILS
//        val response: GetIrrigationZoneDetailsResponseNetworkEntity = webService.getIrrigationZoneDetails(request.toNetworkEntity())
        delay(1000)
        val response = GetIrrigationZoneDetailsResponseNetworkEntity(
            status = 200,
            IrrigationZoneDetailsNetworkEntity(
                uuid = IdentifierUtils.createUUID(),
                name = "JARDIN",
                logoId = R.drawable.ic_card_house,
                colorId = R.color.card_light_blue
            )
        )
        return response.toDomainModel()
    }

}

