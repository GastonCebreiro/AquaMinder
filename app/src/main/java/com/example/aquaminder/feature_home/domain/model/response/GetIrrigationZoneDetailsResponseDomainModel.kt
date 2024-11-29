package com.example.aquaminder.feature_home.domain.model.response

import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel

data class GetIrrigationZoneDetailsResponseDomainModel(
    val status: Int,
    val irrigationZoneDetails: IrrigationZoneDetailsDomainModel
)