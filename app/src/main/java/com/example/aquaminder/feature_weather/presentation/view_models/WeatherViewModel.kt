package com.example.aquaminder.feature_weather.presentation.view_models

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
import com.example.aquaminder.feature_home.data.model.request.GetIrrigationZoneDetailsRequest
import com.example.aquaminder.feature_home.domain.model.ValveDomainModel
import com.example.aquaminder.feature_home.utils.HomeState
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import com.example.aquaminder.feature_weather.use_case.GetWeatherUseCase
import com.example.aquaminder.feature_weather.utils.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val resources: Resources,
    private val getIrrigationZoneIdSelectedUseCase: GetIrrigationZoneIdSelectedUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _weatherState =
        MutableStateFlow<WeatherState>(WeatherState.Idle)
    val weatherState: StateFlow<WeatherState> = _weatherState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getWeather() {
        _isLoading.value = true
        _weatherState.value = WeatherState.Idle

        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data

                    when (val result = getWeatherUseCase.invoke(idSelected)) {
                        is ResultEvent.Success -> {
                            _weatherState.value =
                                WeatherState.Success(result.data)
                        }

                        is ResultEvent.Error -> {
                            when (result.error) {
                                is AppError.WeatherUnavailable -> {
                                    _weatherState.value = WeatherState.Error(
                                        resources.getString(R.string.error_msg_invalid_weather),
                                        R.drawable.ic_cloud_error
                                    )
                                }
                                else -> {
                                    _weatherState.value = WeatherState.Error("")
                                }
                            }

                        }
                    }
                    _isLoading.value = false
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                    _weatherState.value = WeatherState.Error(
                        resources.getString(R.string.error_msg_invalid_id_selected)
                    )
                }
            }
        }

    }
}
