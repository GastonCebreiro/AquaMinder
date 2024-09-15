package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.data.remote.WebService
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.request.toNetworkEntity
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.presentation.model.toNetworkEntity
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.SaveIrrigationZoneResponseNetworkEntity
import com.example.aquaminder.feature_new_irrigation_zone.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
    private val webService: WebService
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: GetIrrigationZonesResponseNetworkEntity = webService.getIrrigationZones(request.toNetworkEntity())
        delay(3000)
        val response = GetIrrigationZonesResponseNetworkEntity(
            status = 200,
            irrigationZones = listOf(
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "Parque",
                    logoId = R.drawable.ic_card_house,
                    colorId = R.color.card_light_blue
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "Balcón",
                    logoId = R.drawable.ic_card_balcony,
                    colorId = R.color.card_blue_pool
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "Patio Trasero",
                    logoId = R.drawable.ic_card_park,
                    colorId = R.color.card_green_water
                ),
                IrrigationZoneNetworkEntity(
                    uuid = IdentifierUtils.createUUID(),
                    name = "Terreza",
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

}

