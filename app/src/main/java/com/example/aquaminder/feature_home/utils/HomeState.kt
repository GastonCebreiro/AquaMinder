package com.example.aquaminder.feature_home.utils

import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel

sealed class HomeState {
    object Idle : HomeState()
    data class Success(val details: IrrigationZoneDetailsDomainModel) : HomeState()
    data class Error(val errorMsg: String) : HomeState()
}
