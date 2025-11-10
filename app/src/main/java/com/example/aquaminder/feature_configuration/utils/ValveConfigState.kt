package com.example.aquaminder.feature_configuration.utils

import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel

sealed class ValveConfigState {
    object Idle : ValveConfigState()
    data class Success(val configuration: IrrigationZoneConfigDomainModel) : ValveConfigState()
    data class Error(val errorMsg: String) : ValveConfigState()
}
