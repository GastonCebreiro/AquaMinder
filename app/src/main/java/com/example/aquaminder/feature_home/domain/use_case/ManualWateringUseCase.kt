package com.example.aquaminder.feature_home.domain.use_case

import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_home.data.model.request.GetValveWateringStatusRequest
import com.example.aquaminder.feature_home.data.model.request.ManualWateringRequest
import com.example.aquaminder.feature_home.data.model.request.toDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveWateringStatusDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import java.io.IOException
import javax.inject.Inject

class ManualWateringUseCase @Inject constructor(
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    suspend operator fun invoke(
        zoneUuid: String,
        water: Boolean
    ): ResultEvent<Boolean> {
        val request = ManualWateringRequest(zoneUuid, water)
        return try {
            val response = irrigationZonesRepository.manualWatering(request)

            if (response.status == STATUS_OK) {
                ResultEvent.Success(
                    true
                )
            } else {
                ResultEvent.Error(AppError.GenericError())
            }
        } catch (e: IOException) {
            ResultEvent.Error(AppError.NetworkError)
        } catch (e: Exception) {
            ResultEvent.Error(AppError.GenericError(e.message.orEmpty()))
        }
    }
}
