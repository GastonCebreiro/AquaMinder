package com.example.aquaminder.feature_main.utils

import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneUiModel

sealed class GetIrrigationZonesEvent {
    data class Success(val items: List<IrrigationZoneUiModel>) : GetIrrigationZonesEvent()
    data class Error(val errorMsg: String) : GetIrrigationZonesEvent()
    data class EmptyList(val errorMsg: String) : GetIrrigationZonesEvent()
}