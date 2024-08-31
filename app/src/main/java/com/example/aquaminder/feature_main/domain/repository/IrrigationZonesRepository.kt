package com.example.aquaminder.feature_main.domain.repository

import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.model.response.NewPasswordResponseDomainModel
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel


interface IrrigationZonesRepository {

    suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel?
}