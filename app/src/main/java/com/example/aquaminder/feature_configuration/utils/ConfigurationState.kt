package com.example.aquaminder.feature_configuration.utils

import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel

sealed class ConfigurationState {
    object Idle : ConfigurationState()
    data class Success(val configuration: IrrigationZoneConfigDomainModel) : ConfigurationState()
    data class Error(val errorMsg: String) : ConfigurationState()
    data class ConfigModified(val configuration: IrrigationZoneConfigDomainModel?, val isModified: Boolean) : ConfigurationState()
}
