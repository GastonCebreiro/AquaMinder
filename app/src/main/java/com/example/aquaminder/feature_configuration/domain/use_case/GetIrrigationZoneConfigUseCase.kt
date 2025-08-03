package com.example.aquaminder.feature_configuration.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_configuration.data.model.request.GetIrrigationZoneConfigRequest
import com.example.aquaminder.feature_configuration.data.model.response.toDomainModel
import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_home.data.model.response.toDomainModel
import com.example.aquaminder.feature_home.data.model.toDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetIrrigationZoneConfigUseCase @Inject constructor(
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    operator fun invoke(
        request: GetIrrigationZoneConfigRequest
    ): Flow<ResultEvent<IrrigationZoneConfigDomainModel>> = flow {
        try {
            val response = irrigationZonesRepository.getIrrigationZoneConfig(request)
            when (response.status) {
                STATUS_OK -> {
                        emit(ResultEvent.Success(response.toDomainModel()))
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