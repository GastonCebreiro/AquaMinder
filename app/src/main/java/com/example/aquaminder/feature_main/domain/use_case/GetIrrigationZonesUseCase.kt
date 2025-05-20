package com.example.aquaminder.feature_main.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_main.data.remote.model.toDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetIrrigationZonesUseCase @Inject constructor(
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    operator fun invoke(
    ): Flow<ResultEvent<List<IrrigationZoneDomainModel>>> = flow {
        try {
            val response = irrigationZonesRepository.getIrrigationZones()
            when (response.status) {
                STATUS_OK -> {
                    val irrigationZones = response.irrigationZones?.map {
                        it.toDomainModel()
                    }.orEmpty()
                    if (irrigationZones.isNotEmpty())
                        emit(ResultEvent.Success(irrigationZones))
                    else
                        emit(ResultEvent.Error(AppError.EmptyList))
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