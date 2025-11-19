package com.example.aquaminder.feature_configuration.presentation.view_models

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.domain.use_case.PlaySoundUseCase
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.core.utils.SoundManager
import com.example.aquaminder.feature_configuration.data.model.request.GetIrrigationZoneConfigRequest
import com.example.aquaminder.feature_configuration.domain.use_case.GetIrrigationZoneConfigUseCase
import com.example.aquaminder.feature_configuration.domain.use_case.SaveConfigUseCase
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_configuration.utils.ValveConfigState
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_home.domain.model.ScheduleDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_login.utils.LoginState
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ValveConfigViewModel @Inject constructor(
    private val resources: Resources,
    private val getIrrigationZoneConfigUseCase: GetIrrigationZoneConfigUseCase,
    private val getIrrigationZoneIdSelectedUseCase: GetIrrigationZoneIdSelectedUseCase,
    private val saveConfigUseCase: SaveConfigUseCase,
    private val playSoundUseCase: PlaySoundUseCase
) : ViewModel() {

    private val _valveConfigState =
        MutableStateFlow<ValveConfigState>(ValveConfigState.Idle)
    val valveConfigState: StateFlow<ValveConfigState> = _valveConfigState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var actualValve: ValveDomainModel = getInitialValve()
    private var valves: MutableList<ValveDomainModel> = mutableListOf()
    private var valveSelectedPosition: Int = 0

    fun restartState() {
        _valveConfigState.value = ValveConfigState.Idle
    }

    fun getIrrigationZoneConfiguration() {
        _isLoading.value = true

        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data

                    getIrrigationZoneConfigUseCase.invoke(idSelected)
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
                                    valves = result.data.valves.toMutableList()
                                    _valveConfigState.value =
                                        ValveConfigState.Success(result.data)
                                }

                                is ResultEvent.Error -> {
                                    when (result.error) {
                                        is AppError.GenericError -> {
                                            _valveConfigState.value = ValveConfigState.Error(
                                                resources.getString(R.string.error_msg_invalid_id_selected)
                                            )
                                        }

                                        else -> {
                                            _valveConfigState.value = ValveConfigState.Error("")
                                        }
                                    }

                                }
                            }
                            _isLoading.value = false
                        }
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                    _valveConfigState.value = ValveConfigState.Error(
                        resources.getString(R.string.error_msg_invalid_id_selected)
                    )
                }
            }
        }
    }

    fun getSelectedValve(position: Int): ValveDomainModel {
        valveSelectedPosition = position
        actualValve = valves[valveSelectedPosition]
        return actualValve
    }

    fun setSwitchSound(isChecked: Boolean) {
        val soundKey = if (isChecked) {
            SoundManager.SOUND_SWITCH_ON
        } else {
            SoundManager.SOUND_SWITCH_OFF
        }

        playSoundUseCase.invoke(soundKey)
    }

    fun saveValveConfig() {
        println(valves)
        _isLoading.value = true
        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data
                    saveConfigUseCase.invoke(idSelected, valves)
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
                                    _valveConfigState.value =
                                        ValveConfigState.ConfigSaved
                                }

                                is ResultEvent.Error -> {
                                    when (result.error) {
                                        is AppError.GenericError -> {
                                            _valveConfigState.value = ValveConfigState.Error(
                                                resources.getString(R.string.error_msg_save_config)
                                            )
                                        }

                                        is AppError.NetworkError -> {
                                            _valveConfigState.value = ValveConfigState.Error(
                                                resources.getString(R.string.error_msg_network),
                                                R.drawable.ic_error_network
                                            )
                                        }

                                        else -> {
                                            _valveConfigState.value = ValveConfigState.Error("Algo fallo")
                                        }
                                    }

                                }
                            }
                            _isLoading.value = false
                        }
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                    _valveConfigState.value = ValveConfigState.Error(
                        resources.getString(R.string.error_msg_invalid_id_selected)
                    )
                }
            }
        }
    }

    private fun getInitialValve(): ValveDomainModel =
        ValveDomainModel(
            0,
            ControlMode.SCHEDULED,
            false,
            0,
            100,
            ScheduleDomainModel(
                FrequencyMode.INTERVAL_DAYS,
                1,
                emptyList(),
                emptyList(),
                0
            ),
            false,
            isWatering = false,
            lastWaters = emptyList(),
            lastHumidity = emptyList()
        )

    fun setActiveChecked(isChecked: Boolean) {
        actualValve = actualValve.copy(
            isActive = isChecked
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setWeatherChecked(isChecked: Boolean) {
        actualValve = actualValve.copy(
            isWeatherChecked = isChecked
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setControlMode(controlMode: ControlMode) {
        actualValve = actualValve.copy(
            controlMode = controlMode
        )
        valves[valveSelectedPosition] = actualValve
    }


    fun setFrequencyMode(frequencyMode: FrequencyMode) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                frequencyMode = frequencyMode
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setIntervalDays(days: Int) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                intervalDays = days
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun addDayOfWeek(day: DayOfWeek) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                daysOfWeek = (actualValve.schedule?.daysOfWeek ?: emptyList()) + day
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun removeDayOfWeek(day: DayOfWeek) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                daysOfWeek = (actualValve.schedule?.daysOfWeek ?: emptyList()) - day
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun addTime(pickedTime: LocalTime) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                waterTimes = (actualValve.schedule?.waterTimes ?: emptyList()) + pickedTime
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun removeTime(pickedTime: LocalTime) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                waterTimes = (actualValve.schedule?.waterTimes ?: emptyList()) - pickedTime
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setDuration(duration: Int) {
        actualValve = actualValve.copy(
            schedule = actualValve.schedule?.copy(
                duration = duration
            )
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setHumidityMin(minHum: Int) {
        actualValve = actualValve.copy(
            humidityMin = minHum
        )
        valves[valveSelectedPosition] = actualValve
    }

    fun setHumidityMax(maxHum: Int) {
        actualValve = actualValve.copy(
            humidityMax = maxHum
        )
        valves[valveSelectedPosition] = actualValve
    }

}