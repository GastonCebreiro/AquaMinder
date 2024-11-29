package com.example.aquaminder.feature_new_irrigation_zone.utils

sealed class NewIrrigationZoneState {
    object Idle : NewIrrigationZoneState()
    data class Success(val message: String) : NewIrrigationZoneState()
    data class Error(val errorMsg: String, val logoId: Int? = null) : NewIrrigationZoneState()
    data class CreatedId(val id: String) : NewIrrigationZoneState()
}
