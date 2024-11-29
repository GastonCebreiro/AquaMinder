package com.example.aquaminder.feature_home.data.model.response

import com.example.aquaminder.core.utils.AppConstants
import com.example.aquaminder.feature_home.data.model.IrrigationZoneDetailsNetworkEntity
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_home.domain.model.response.GetIrrigationZoneDetailsResponseDomainModel

data class GetIrrigationZoneDetailsResponseNetworkEntity(
    val status: Int? = null,
    val irrigationZoneDetails: IrrigationZoneDetailsNetworkEntity? = null
)

fun GetIrrigationZoneDetailsResponseNetworkEntity.toDomainModel() = GetIrrigationZoneDetailsResponseDomainModel(
    status = status ?: -1,
    irrigationZoneDetails = IrrigationZoneDetailsDomainModel(
        uuid = irrigationZoneDetails?.uuid.orEmpty(),
        name = irrigationZoneDetails?.name ?: AppConstants.DEFAULT_IZ_NAME,
        logoId = irrigationZoneDetails?.logoId ?: AppConstants.DEFAULT_LOGO_ID,
        colorId = irrigationZoneDetails?.colorId ?: AppConstants.DEFAULT_COLOR_ID
    )
)