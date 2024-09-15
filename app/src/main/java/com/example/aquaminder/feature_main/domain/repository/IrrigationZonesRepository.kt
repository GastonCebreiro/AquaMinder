package com.example.aquaminder.feature_main.domain.repository

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_new_irrigation_zone.domain.model.response.SaveIrrigationZoneResponseDomainModel


interface IrrigationZonesRepository {

    suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel

    suspend fun saveIrrigationZone(request: IrrigationZoneDomainModel): SaveIrrigationZoneResponseDomainModel
}