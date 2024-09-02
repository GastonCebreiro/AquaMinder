package com.example.aquaminder.feature_main.domain.model.response

import com.example.aquaminder.core.utils.AppConstants.DEFAULT_COLOR_ID
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_IZ_NAME
import com.example.aquaminder.core.utils.AppConstants.DEFAULT_LOGO_ID
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneUiModel
import java.util.UUID

data class GetIrrigationZonesResponseDomainModel(
    val status: Int,
    val irrigationZones: List<IrrigationZoneDomainModel>
)

data class IrrigationZoneDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var colorId: Int
)

fun List<IrrigationZoneDomainModel>.toUiModelList() = this.map { domainModel ->
    domainModel.toUiModel()
}

fun IrrigationZoneDomainModel.toUiModel() = IrrigationZoneUiModel(
    uuid = uuid.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString(),
    name = name.takeIf { it.isNotBlank() } ?: DEFAULT_IZ_NAME,
    logoId = logoId.takeIf { it > 0 } ?: DEFAULT_LOGO_ID,
    colorId = colorId.takeIf { it > 0 } ?: DEFAULT_COLOR_ID
)