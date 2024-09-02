package com.example.aquaminder.feature_main.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.use_case.GetUserLoggedUseCase
import com.example.aquaminder.feature_login.utils.LoginState
import com.example.aquaminder.feature_main.domain.model.request.GetIrrigationZonesRequestDomainModel
import com.example.aquaminder.feature_main.domain.use_case.GetIrrigationZonesUseCase
import com.example.aquaminder.feature_main.utils.GetIrrigationZonesEvent
import com.example.aquaminder.feature_main.utils.IrrigationZoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IrrigationZonesViewModel @Inject constructor(
    private val resources: Resources,
    private val getUserLoggedUseCase: GetUserLoggedUseCase,
    private val getIrrigationZonesUseCase: GetIrrigationZonesUseCase
) : ViewModel() {

    private val _irrigationZoneState =
        MutableStateFlow<IrrigationZoneState>(IrrigationZoneState.Idle)
    val irrigationZoneState: StateFlow<IrrigationZoneState> = _irrigationZoneState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun updateViewState(viewState: IrrigationZoneState) {
        _irrigationZoneState.value = viewState
    }

    fun getIrrigationZones() {
        _isLoading.value = true

        viewModelScope.launch {
            when (val res = getUserLoggedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val userLogged = res.data

                    getIrrigationZonesUseCase.invoke(GetIrrigationZonesRequestDomainModel(userLogged.name))
                        .collect { result ->
                            when (result) {
                                is ResultEvent.Success -> {
                                    _irrigationZoneState.value =
                                        IrrigationZoneState.Success(result.data)
                                }
                                is ResultEvent.Error -> {
                                    when (result.error) {
                                        is AppError.EmptyList -> {
                                            _irrigationZoneState.value = IrrigationZoneState.EmptyList(
                                                resources.getString(R.string.error_msg_ir_empty_list)
                                            )
                                        }
                                        is AppError.GenericError -> {
                                            _irrigationZoneState.value = IrrigationZoneState.Error(
                                                resources.getString(R.string.error_msg_ir_generic)
                                            )
                                        }
                                        else -> {
                                            _irrigationZoneState.value = IrrigationZoneState.Error("")
                                        }
                                    }

                                }
                            }
                            _isLoading.value = false
                        }
                }

                is ResultEvent.Error -> {
                    _isLoading.value = false
                }
            }
        }
    }

}