package com.example.aquaminder.feature_configuration.presentation.view_models

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_home.domain.model.request.GetIrrigationZoneDetailsRequestDomainModel
import com.example.aquaminder.feature_home.domain.use_case.GetIrrigationZoneDetailsUseCase
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZoneIdSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val resources: Resources,
    private val getIrrigationZoneDetailsUseCase: GetIrrigationZoneDetailsUseCase,
    private val getIrrigationZoneIdSelectedUseCase: GetIrrigationZoneIdSelectedUseCase
) : ViewModel() {

    private val _configurationState =
        MutableStateFlow<ConfigurationState>(ConfigurationState.Idle)
    val configurationState: StateFlow<ConfigurationState> = _configurationState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getIrrigationZoneDetails() {
        _isLoading.value = true
        _configurationState.value = ConfigurationState.Idle
        
        viewModelScope.launch {
            when (val res = getIrrigationZoneIdSelectedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val idSelected = res.data

                    getIrrigationZoneDetailsUseCase.invoke(GetIrrigationZoneDetailsRequestDomainModel(idSelected))
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
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

}