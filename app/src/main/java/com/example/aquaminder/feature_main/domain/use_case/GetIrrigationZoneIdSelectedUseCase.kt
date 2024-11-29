package com.example.aquaminder.feature_main.domain.use_case

import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import javax.inject.Inject

class GetIrrigationZoneIdSelectedUseCase @Inject constructor(
    private val irrigationZoneRepository: IrrigationZonesRepository
) {

    operator fun invoke(): ResultEvent<String>  {
        val id = irrigationZoneRepository.getIrrigationZoneIdSelected()
        return if (id.isBlank()) {
            ResultEvent.Error(AppError.GenericError())
        } else {
            ResultEvent.Success(id)
        }
    }
}