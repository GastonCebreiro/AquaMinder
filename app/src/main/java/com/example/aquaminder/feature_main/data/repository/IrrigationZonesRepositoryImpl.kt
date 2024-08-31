package com.example.aquaminder.feature_main.data.repository

import com.example.aquaminder.feature_main.data.remote.model.response.GetIrrigationZonesResponseNetworkEntity
import com.example.aquaminder.feature_main.data.remote.model.response.toDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.GetIrrigationZonesResponseDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import javax.inject.Inject

class IrrigationZonesRepositoryImpl @Inject constructor(
) : IrrigationZonesRepository {

    override suspend fun getIrrigationZones(request: GetIrrigationZonesRequestDomainModel): GetIrrigationZonesResponseDomainModel? {
        // TODO GC ADD SERVICE CALL FOR IRRIGATION ZONES
//        val response: GetIrrigationZonesResponseNetworkEntity = irrigationZonesService.getIrrigationZones(request)
        val response = GetIrrigationZonesResponseNetworkEntity()
        return response.toDomainModel()
    }

}

