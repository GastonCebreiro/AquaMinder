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
import com.example.aquaminder.feature_configuration.domain.model.IrrigationZoneConfigDomainModel
import com.example.aquaminder.feature_configuration.domain.use_case.GetIrrigationZoneConfigUseCase
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val resources: Resources,
    private val getIrrigationZoneConfigUseCase: GetIrrigationZoneConfigUseCase,
    private val getIrrigationZoneIdSelectedUseCase: GetIrrigationZoneIdSelectedUseCase,
    private val playSoundUseCase: PlaySoundUseCase
) : ViewModel() {

    private val _configurationState =
        MutableStateFlow<ConfigurationState>(ConfigurationState.Idle)
    val configurationState: StateFlow<ConfigurationState> = _configurationState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var initialConfiguration: IrrigationZoneConfigDomainModel? = null
    private var actualConfiguration: IrrigationZoneConfigDomainModel? = null

    fun restartState() {
        _configurationState.value = ConfigurationState.Idle
    }

    fun getIrrigationZoneConfiguration() {
        restartState()
        _isLoading.value = true

        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data

                    getIrrigationZoneConfigUseCase.invoke(GetIrrigationZoneConfigRequest(idSelected))
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
                                    initialConfiguration = result.data.copy()
                                    actualConfiguration = result.data.copy()
                                    _configurationState.value =
                                        ConfigurationState.Success(result.data)
                                }

                                is ResultEvent.Error -> {
                                    when (result.error) {
                                        is AppError.GenericError -> {
                                            _configurationState.value = ConfigurationState.Error(
                                                resources.getString(R.string.error_msg_invalid_id_selected)
                                            )
                                        }

                                        else -> {
                                            _configurationState.value = ConfigurationState.Error("")
                                        }
                                    }

                                }
                            }
                            _isLoading.value = false
                        }
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                    _configurationState.value = ConfigurationState.Error(
                        resources.getString(R.string.error_msg_invalid_id_selected)
                    )
                }
            }
        }
    }

//    fun setNewHumidity(valve: ValveDomainModel, newHumidity: Int) {
//        actualConfiguration = actualConfiguration?.copy(
//            valves = actualConfiguration?.valves?.map {
//                if (it.id == valve.id) it.copy(humidity = newHumidity)
//                else it
//            }.orEmpty()
//        )
//        checkNewConfig()
//    }
//
//    fun getNewHumidity(valve: ValveDomainModel): Int =
//        actualConfiguration?.valves?.find {
//            it.id == valve.id
//        }?.humidity ?: 0
//
//    fun setNewStartHour(valve: ValveDomainModel, newStartHour: LocalTime) {
//        actualConfiguration = actualConfiguration?.copy(
//            valves = actualConfiguration?.valves?.map {
//                if (it.id == valve.id) it.copy(schedule = it.schedule?.copy(startTime = newStartHour))
//                else it
//            }.orEmpty()
//        )
//        checkNewConfig()
//    }
//
//    fun setNewIntervalHours(valve: ValveDomainModel, newIntervalHours: Int) {
//        actualConfiguration = actualConfiguration?.copy(
//            valves = actualConfiguration?.valves?.map {
//                if (it.id == valve.id) it.copy(schedule = it.schedule?.copy(intervalHours = newIntervalHours))
//                else it
//            }.orEmpty()
//        )
//        checkNewConfig()
//    }

    fun setNewDuration(valve: ValveDomainModel, newDuration: Int) {
        actualConfiguration = actualConfiguration?.copy(
            valves = actualConfiguration?.valves?.map {
                if (it.id == valve.id) it.copy(schedule = it.schedule?.copy(duration = newDuration))
                else it
            }.orEmpty()
        )
        checkNewConfig()
    }

    fun setCheckHumidity(isChecked: Boolean) {
        actualConfiguration = actualConfiguration?.copy(
            isCheckHumidityEnabled = isChecked
        )
        checkNewConfig()
    }

    fun setCheckWeather(isChecked: Boolean) {
        actualConfiguration = actualConfiguration?.copy(
            isCheckWeatherEnabled = isChecked
        )
        checkNewConfig()
    }

    private fun checkNewConfig() {
        _configurationState.value = ConfigurationState.ConfigModified(
            configuration = actualConfiguration,
            isModified = actualConfiguration != initialConfiguration
        )
    }

    fun saveConfiguration() {
        println(actualConfiguration)
    }

    fun setSwitchSound(isChecked: Boolean) {
        val soundKey = if (isChecked) {
            SoundManager.SOUND_SWITCH_ON
        } else {
            SoundManager.SOUND_SWITCH_OFF
        }

        playSoundUseCase.invoke(soundKey)
    }

}