package com.example.aquaminder.feature_configuration.utils

import com.example.aquaminder.feature_home.domain.model.ValveDomainModel

sealed class ValveConfigState {
    object Idle : ValveConfigState()
    object NewValve : ValveConfigState()
    data class EditValve(val valve: ValveDomainModel) : ValveConfigState()
    data class Error(val errorMsg: String) : ValveConfigState()
}
