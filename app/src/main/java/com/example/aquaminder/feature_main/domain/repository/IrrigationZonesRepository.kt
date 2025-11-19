package com.example.aquaminder.feature_main.domain.repository

import com.example.aquaminder.feature_configuration.data.model.request.GetIrrigationZoneConfigRequest
import com.example.aquaminder.feature_configuration.data.model.request.IrrigationZoneConfigNetworkEntity
import com.example.aquaminder.feature_configuration.data.model.response.GetIrrigationZoneConfigResponse
import com.example.aquaminder.feature_configuration.data.model.response.SaveIrrigationZoneConfigResponse
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.data.model.response.GetIrrigationZoneDetailsResponse
import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponse
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel


interface IrrigationZonesRepository {

    suspend fun getIrrigationZones(): GetIrrigationZonesResponse

    suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel

    fun saveIrrigationZoneIdSelected(id: String)

    fun getIrrigationZoneIdSelected(): String

    suspend fun getIrrigationZoneDetails(request: GetIrrigationZoneDetailsRequest): GetIrrigationZoneDetailsResponse

    suspend fun saveConfig(request: IrrigationZoneConfigNetworkEntity): SaveIrrigationZoneConfigResponse

    suspend fun getIrrigationZoneConfig(request: GetIrrigationZoneConfigRequest): GetIrrigationZoneConfigResponse
}