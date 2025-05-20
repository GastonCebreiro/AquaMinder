package com.example.aquaminder.feature_new_irrigation_zone.presentation.view_model

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.IdentifierUtils
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_main.domain.model.Address
import com.example.aquaminder.feature_main.domain.model.IrrigationZoneDomainModel
import com.example.aquaminder.feature_main.domain.model.Location
import com.example.aquaminder.feature_new_irrigation_zone.domain.use_case.SaveIrrigationZoneUseCase
import com.example.aquaminder.feature_new_irrigation_zone.utils.IrrigationZoneUtils
import com.example.aquaminder.feature_new_irrigation_zone.utils.NewIrrigationZoneState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewIrrigationZoneViewModel @Inject constructor(
    private val resources: Resources,
    private val saveIrrigationZoneUseCase: SaveIrrigationZoneUseCase,
) : ViewModel() {

    private val _irrigationZoneState =
        MutableStateFlow<NewIrrigationZoneState>(NewIrrigationZoneState.Idle)
    val irrigationZoneState: StateFlow<NewIrrigationZoneState> = _irrigationZoneState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var location: Location? = null

    fun checkValidID(id: String) {
        _irrigationZoneState.value = NewIrrigationZoneState.Idle
        if (IdentifierUtils.isValidID(id))
            _irrigationZoneState.value = NewIrrigationZoneState.ValidID(id)
        else
            _irrigationZoneState.value = NewIrrigationZoneState.InvalidID
    }

    fun saveLocation(lat: Double, lon: Double, address: Address) {
        location = Location(
            latitude = lat,
            longitude = lon,
            address = address
        )

    }

    fun saveIrrigationZone(
        inputId: String,
        inputName: String,
        inputLogo: Int
    ) {
        _irrigationZoneState.value = NewIrrigationZoneState.Idle
        val id = inputId.trim()
        val name = inputName.trim()
        val logo = IrrigationZoneUtils.getLogos()[inputLogo]
        val color = IrrigationZoneUtils.getColors()[inputLogo]

        when {
            id.isBlank() -> {
                _irrigationZoneState.value = NewIrrigationZoneState.Error(
                    resources.getString(R.string.error_msg_new_irrigation_zone_invalid_id)
                )
                return
            }
            name.isBlank() -> {
                _irrigationZoneState.value = NewIrrigationZoneState.Error(
                    resources.getString(R.string.error_msg_new_irrigation_zone_invalid_name)
                )
                return
            }
            location == null -> {
                _irrigationZoneState.value = NewIrrigationZoneState.Error(
                    resources.getString(R.string.error_msg_new_irrigation_zone_invalid_address)
                )
                return
            }

            else -> {
                saveNewIrrigationZone(id, name, logo, color)
            }
        }
    }

    private fun saveNewIrrigationZone(id: String, name: String, logo: Int, color: Int) {
        _isLoading.value = true
        _irrigationZoneState.value = NewIrrigationZoneState.Idle

        viewModelScope.launch {
            saveIrrigationZoneUseCase.invoke(
                IrrigationZoneDomainModel(
                    uuid = id,
                    name = name,
                    logoId = logo,
                    colorId = color,
                    location = location!!
                )
            ).collect { result ->
                when (result) {
                    is ResultEvent.Success -> {
                        _irrigationZoneState.value =
                            NewIrrigationZoneState.Success(resources.getString(R.string.fragment_new_irrigation_zone_success_message))
                    }

                    is ResultEvent.Error -> {
                        when (result.error) {
                            is AppError.GenericError -> {
                                _irrigationZoneState.value = NewIrrigationZoneState.Error(
                                    resources.getString(R.string.error_msg_new_iz)
                                )
                            }

                            is AppError.NetworkError -> {
                                _irrigationZoneState.value =
                                    NewIrrigationZoneState.Error(
                                        resources.getString(R.string.error_msg_network),
                                        R.drawable.ic_error_network
                                    )
                            }

                            else -> {
                                _irrigationZoneState.value = NewIrrigationZoneState.Error("")
                            }
                        }

                    }
                }
                _isLoading.value = false
            }
        }
        Log.d("GASTON", "id=$id\nname=$name\nlogo=$logo\ncolor=$color")
    }


}