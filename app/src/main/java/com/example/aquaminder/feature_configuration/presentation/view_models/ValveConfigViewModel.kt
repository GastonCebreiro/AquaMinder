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
import com.example.aquaminder.feature_configuration.utils.ValveConfigState
import com.example.aquaminder.feature_home.domain.model.ControlMode
import com.example.aquaminder.feature_home.domain.model.FrequencyMode
import com.example.aquaminder.feature_home.domain.model.ScheduleDomainModel
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
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
    private val playSoundUseCase: PlaySoundUseCase
) : ViewModel() {

    private val _valveConfigState =
        MutableStateFlow<ValveConfigState>(ValveConfigState.Idle)
    val valveConfigState: StateFlow<ValveConfigState> = _valveConfigState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var initialValve: ValveDomainModel? = null

    fun getIrrigationZoneConfiguration() {
        _isLoading.value = true

        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data

                    getIrrigationZoneConfigUseCase.invoke(GetIrrigationZoneConfigRequest(idSelected))
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
//                                    _valveConfigState.value =
//                                        ValveConfigState.Success(result.data)
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

    fun setSelectedValve(valve: ValveDomainModel?) {
        initialValve = valve?.copy() ?: getInitialValve()
        valve?.let {
            _valveConfigState.value = ValveConfigState.EditValve(valve)
        } ?: run {
            _valveConfigState.value = ValveConfigState.NewValve
        }
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
        println(initialValve)
    }

    fun createNewValve() {
        println(initialValve)
    }

    fun setWeatherChecked(isChecked: Boolean) {
        initialValve = initialValve?.copy(
            isWeatherChecked = isChecked
        )
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
            false
        )

    fun setControlMode(controlMode: ControlMode) {
        initialValve = initialValve?.copy(
            controlMode = controlMode
        )
    }

    fun setFrequencyMode(frequencyMode: FrequencyMode) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                frequencyMode = frequencyMode
            )
        )
    }

    fun setIntervalDays(days: Int) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                intervalDays = days
            )
        )
    }

    fun addDayOfWeek(day: DayOfWeek) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                daysOfWeek = (initialValve?.schedule?.daysOfWeek ?: emptyList()) + day
            )
        )
    }

    fun removeDayOfWeek(day: DayOfWeek) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                daysOfWeek = (initialValve?.schedule?.daysOfWeek ?: emptyList()) - day
            )
        )
    }

    fun addTime(pickedTime: LocalTime) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                waterTimes = (initialValve?.schedule?.waterTimes ?: emptyList()) + pickedTime
            )
        )
    }

    fun removeTime(pickedTime: LocalTime) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                waterTimes = (initialValve?.schedule?.waterTimes ?: emptyList()) - pickedTime
            )
        )
    }

    fun setDuration(duration: Int) {
        initialValve = initialValve?.copy(
            schedule = initialValve?.schedule?.copy(
                duration = duration
            )
        )
    }

    fun setHumidityMin(minHum: Int) {
        initialValve = initialValve?.copy(
            humidityMin = minHum
        )
    }

    fun setHumidityMax(maxHum: Int) {
        initialValve = initialValve?.copy(
            humidityMax = maxHum
        )
    }

}