package com.example.aquaminder.feature_main.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.response.toUiModelList
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetIrrigationZonesUseCase @Inject constructor(
    private val resources: Resources,
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    suspend operator fun invoke(
        request: GetIrrigationZonesRequestDomainModel
    ): Flow<ResultEvent<List<IrrigationZoneUiModel>>> = flow {
        try {
            val response = irrigationZonesRepository.getIrrigationZones(request)
            when (response?.status) {
                STATUS_OK -> {
                    if (response.irrigationZones.isNotEmpty())
                        emit(ResultEvent.Success(response.irrigationZones.toUiModelList()))
                    else
                        emit(ResultEvent.Error(AppError.EmptyList))
                }
                else -> {
                    emit(ResultEvent.Error(AppError.GenericError()))
                }
            }

        } catch (e: Exception) {
            emit(ResultEvent.Error(AppError.GenericError(e.message.orEmpty())))
        }
    }
}