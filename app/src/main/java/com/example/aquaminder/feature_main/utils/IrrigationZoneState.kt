package com.example.aquaminder.feature_main.utils

import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel

sealed class IrrigationZoneState {
    object Idle : IrrigationZoneState()
    data class Success(val itemList: List<IrrigationZoneDomainModel>) : IrrigationZoneState()
    data class Error(val errorMsg: String) : IrrigationZoneState()
    data class EmptyList(val emptyListMsg: String) : IrrigationZoneState()
}
