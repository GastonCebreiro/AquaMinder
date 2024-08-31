package com.example.aquaminder.feature_main.domain.use_case

import android.content.res.Resources
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppConstants.STATUS_OK
import com.example.aquaminder.feature_login.utils.NewPasswordEvent
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetIrrigationZonesUseCase @Inject constructor(
    private val resources: Resources,
    private val irrigationZonesRepository: IrrigationZonesRepository
) {

    suspend operator fun invoke(
        request: GetIrrigationZonesRequestDomainModel
    ): Flow<NewPasswordEvent> = flow {
        try {
            val response = irrigationZonesRepository.getIrrigationZones(request)
            when (response?.status) {
                STATUS_OK -> {
                    emit(NewPasswordEvent.Success(response.message))
                }
                else -> {
                    emit(NewPasswordEvent.UsernameNotFound(resources.getString(R.string.error_msg_new_user_not_found)))
                }
            }

        } catch (e: Exception) {
            val errorMsg = "${resources.getString(R.string.error_msg_unknown)}: ${e.message}"
            emit(NewPasswordEvent.Error(errorMsg))
        }
    }
}