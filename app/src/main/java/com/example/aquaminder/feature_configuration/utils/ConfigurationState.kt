package com.example.aquaminder.feature_configuration.utils

import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel

sealed class ConfigurationState {
    object Idle : ConfigurationState()
    data class Success(val configuration: IrrigationZoneDetailsDomainModel) : ConfigurationState()
    data class Error(val errorMsg: String) : ConfigurationState()
}
