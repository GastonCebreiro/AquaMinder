package com.example.aquaminder.feature_home.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_home.domain.model.request.GetIrrigationZoneDetailsRequestDomainModel
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetIrrigationZoneDetailsUseCase @Inject constructor(
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    suspend operator fun invoke(
        request: GetIrrigationZoneDetailsRequestDomainModel
    ): Flow<ResultEvent<IrrigationZoneDetailsDomainModel>> = flow {
        try {
            val response = irrigationZonesRepository.getIrrigationZoneDetails(request)
            when (response.status) {
                STATUS_OK -> {
                    emit(ResultEvent.Success(response.irrigationZoneDetails))
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