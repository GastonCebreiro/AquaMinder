package com.example.aquaminder.feature_configuration.utils

import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel

sealed class ValveConfigState {
    object Idle : ValveConfigState()
    data class Success(val configuration: IrrigationZoneDetailsDomainModel) : ValveConfigState()
    data class Error(val errorMsg: String, val logoId: Int? = null) : ValveConfigState()
    object ConfigSaved: ValveConfigState()
}
