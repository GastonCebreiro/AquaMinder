package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.R
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.IrrigationZoneNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.request.toNetworkEntity
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import java.util.UUID
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: GetIrrigationZonesResponseNetworkEntity = irrigationZonesService.getIrrigationZones(request.toNetworkEntity())
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
//                IrrigationZoneNetworkEntity(
//                    uuid = IdentifierUtils.createUUID(),
//                    name = "Patio Trasero",
//                    logoId = R.drawable.ic_card_park,
//                    colorId = R.color.card_green_water
//                ),
//                IrrigationZoneNetworkEntity(
//                    uuid = IdentifierUtils.createUUID(),
//                    name = "Terreza",
//                    logoId = R.drawable.ic_card_flowers,
//                    colorId = R.color.card_gray
//                )
            )
        )
        return response.toDomainModel()
    }

}

