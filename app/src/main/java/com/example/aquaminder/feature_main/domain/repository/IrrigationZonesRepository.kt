package com.example.aquaminder.feature_main.domain.repository

import com.example.aquaminder.feature_home.domain.model.request.GetIrrigationZoneDetailsRequestDomainModel
import com.example.aquaminder.feature_home.domain.model.response.GetIrrigationZoneDetailsResponseDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel


interface IrrigationZonesRepository {

    suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel

    suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel

    fun saveIrrigationZoneIdSelected(id: String)

    fun getIrrigationZoneIdSelected(): String

    suspend fun getIrrigationZoneDetails(request: GetIrrigationZoneDetailsRequestDomainModel): GetIrrigationZoneDetailsResponseDomainModel
}