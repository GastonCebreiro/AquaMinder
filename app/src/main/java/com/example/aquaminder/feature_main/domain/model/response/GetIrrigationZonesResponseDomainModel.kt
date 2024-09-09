package com.example.aquaminder.feature_main.domain.model.response

import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel

data class GetIrrigationZonesResponseDomainModel(
    val status: Int,
    val irrigationZones: List<IrrigationZoneDomainModel>
)