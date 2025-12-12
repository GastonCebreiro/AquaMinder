package com.example.aquaminder.feature_configuration.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_configuration.data.model.request.IrrigationZoneConfigNetworkEntity
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_home.domain.model.toNetworkEntity
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SaveConfigUseCase @Inject constructor(
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    operator fun invoke(
        id: String,
        valves: List<ValveDomainModel>
    ): Flow<ResultEvent<String>> = flow {
        try {
            val response = irrigationZonesRepository.saveConfig(
                IrrigationZoneConfigNetworkEntity(id, valves.map { it.toNetworkEntity() })
            )
            when (response.status) {
                STATUS_OK -> {
                    emit(ResultEvent.Success(""))
                }

                else -> {
                    emit(ResultEvent.Error(AppError.GenericError()))
                }
            }
        } catch (e: IOException) {
            emit(ResultEvent.Error(AppError.NetworkError))
        } catch (e: Exception) {
            emit(ResultEvent.Error(AppError.GenericError(e.message.orEmpty())))
        }
    }
}