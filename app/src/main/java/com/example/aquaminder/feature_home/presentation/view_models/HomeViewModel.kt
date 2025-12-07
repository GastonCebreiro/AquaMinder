package com.example.aquaminder.feature_home.presentation.view_models

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.domain.use_case.GetIrrigationZoneDetailsUseCase
import com.example.aquaminder.feature_home.domain.use_case.GetValveWateringStatusUseCase
import com.example.aquaminder.feature_home.domain.use_case.ManualWateringUseCase
import com.example.aquaminder.feature_home.utils.HomeState
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.delay


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val resources: Resources,
    private val getIrrigationZoneDetailsUseCase: GetIrrigationZoneDetailsUseCase,
    private val getIrrigationZoneIdSelectedUseCase: GetIrrigationZoneIdSelectedUseCase,
    private val getValveWateringStatusUseCase: GetValveWateringStatusUseCase,
    private val manualWateringUseCase: ManualWateringUseCase
) : ViewModel() {

    private val _homeState =
        MutableStateFlow<HomeState>(HomeState.Idle)
    val homeState: StateFlow<HomeState> = _homeState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _wateringState = MutableStateFlow(false)
    val wateringState: StateFlow<Boolean> = _wateringState

    private var pollingJob: Job? = null

    private var idSelected: String = ""

    fun getIrrigationZoneDetails() {
        _isLoading.value = true
        _homeState.value = HomeState.Idle

        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    idSelected = res.data

                    getIrrigationZoneDetailsUseCase.invoke(
                        GetIrrigationZoneDetailsRequest(
                            idSelected
                        )
                    )
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
                                    _homeState.value =
                                        HomeState.Success(result.data)
                                }

                                is ResultEvent.Error -> {
                                    when (result.error) {
                                        is AppError.GenericError -> {
                                            _homeState.value = HomeState.Error(
                                                resources.getString(R.string.error_msg_catch, result.error.errorMsg)
                                            )
                                        }

                                        else -> {
                                            _homeState.value = HomeState.Error("")
                                        }
                                    }

                                }
                            }
                            _isLoading.value = false
                        }
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                    _homeState.value = HomeState.Error(
                        resources.getString(R.string.error_msg_invalid_id_selected)
                    )
                }
            }
        }
    }

    fun startPolling(valveId: Int) {
        pollingJob?.cancel()

        pollingJob = viewModelScope.launch {
            while (isActive) {
                val result = getValveWateringStatusUseCase.invoke(idSelected, valveId)

                if (result is ResultEvent.Success) {
                    Log.d("GASTON", "isWatering=${result.data.isWatering}")
                    if (_wateringState.value != result.data.isWatering) {
                        _wateringState.value = result.data.isWatering
                    }
                }

                delay(POLLING_TIME)
            }
        }
    }

    companion object {
        private const val POLLING_TIME = 3000L
    }

    fun stopPolling() {
        pollingJob?.cancel()
    }

    fun manualWatering(water: Boolean) {
        _homeState.value = HomeState.Idle
        _homeState.value = HomeState.ManualWatering(true)
        viewModelScope.launch {
            val result = manualWateringUseCase.invoke(
                zoneUuid = idSelected,
                water = water
            )
             when(result) {
                 is ResultEvent.Success -> {
                     _homeState.value = HomeState.Idle
                 }
                 is ResultEvent.Error -> {
                     _homeState.value = HomeState.ManualWatering(false)
                     when (result.error) {
                         is AppError.GenericError -> {
                             _homeState.value = HomeState.Error(
                                 resources.getString(R.string.error_msg_catch, result.error.errorMsg)
                             )
                         }

                         else -> {
                             _homeState.value = HomeState.Error("")
                         }
                     }
                 }
             }
        }
    }

}