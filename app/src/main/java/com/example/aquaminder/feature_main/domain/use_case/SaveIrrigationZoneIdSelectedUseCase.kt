package com.example.aquaminder.feature_main.domain.use_case

import com.example.aquaminder.core.utils.SharedPreferencesUtil
import com.example.aquaminder.feature_login.domain.repository.UserRepository
import com.example.aquaminder.feature_main.domain.repository.IrrigationZonesRepository
import javax.inject.Inject

class SaveIrrigationZoneIdSelectedUseCase @Inject constructor(
    private val irrigationZoneRepository: IrrigationZonesRepository
) {

    operator fun invoke(
        id: String
    ) {
        irrigationZoneRepository.saveIrrigationZoneIdSelected(id)
    }
}