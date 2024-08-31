package com.example.aquaminder.feature_main.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.use_case.GetUserLoggedUseCase
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
        MutableStateFlow<IrrigationZoneState>(IrrigationZoneState.Loading(false))
    val irrigationZoneState: StateFlow<IrrigationZoneState> = _irrigationZoneState


    fun getIrrigationZones() {
        viewModelScope.launch {
            _irrigationZoneState.value = IrrigationZoneState.Loading(true)

            when (val res = getUserLoggedUseCase.invoke()) {
                is ResultEvent.Success -> {
                    val userLogged = res.data

                    getIrrigationZonesUseCase.invoke(userLogged)
                        .collect { res ->
                            when (res) {
                                is GetIrrigationZonesEvent.Success -> {
                                    _irrigationZoneState.value =
                                        IrrigationZoneState.Success(res.items)
                                }

                                is GetIrrigationZonesEvent.EmptyList {
                                    _irrigationZoneState.value = IrrigationZoneState.EmptyList(
                                        resources.getString(R.string.error_msg_ir_empty_list)
                                    )
                                }

                                _irrigationZoneState.value = IrrigationZoneState.Loading(false)
                            }
                        }


                }

                is ResultEvent.Error -> {
                    _irrigationZoneState.value = IrrigationZoneState.Error(res.errorMsg)
                    _irrigationZoneState.value = IrrigationZoneState.Loading(false)
                    return@launch
                }
            }

        }
    }

}