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
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private var actualValve: ValveDomainModel? = null

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
        initialValve = valve?.copy()
        actualValve = valve?.copy()
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
        println(actualValve)
    }

    fun createNewValve() {
        println(actualValve)
    }

    fun setHumidity(selectedHumidity: Int) {
        actualValve = actualValve?.copy(
            humidity = selectedHumidity
        )
        checkModifications()
    }

    fun setStartTime(selectedTime: LocalTime) {
        actualValve = actualValve?.copy(
            schedule = actualValve?.schedule?.copy(
                startTime = selectedTime
            )
        )
        checkModifications()
    }

    fun setIntervalHours(selectedIntervalHours: Int) {
        actualValve = actualValve?.copy(
            schedule = actualValve?.schedule?.copy(
                intervalHours = selectedIntervalHours
            )
        )
        checkModifications()
    }

    fun setDuration(selectedDuration: Int) {
        actualValve = actualValve?.copy(
            schedule = actualValve?.schedule?.copy(
                duration = selectedDuration
            )
        )
        checkModifications()
    }

    private fun checkModifications() {
        actualValve?.let {
            _valveConfigState.value = ValveConfigState.EditValve(
                valve = it,
                isModified = it != initialValve
            )
        }
    }

}