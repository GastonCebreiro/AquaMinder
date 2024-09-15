package com.example.aquaminder.feature_main.presentation.model

import android.os.Parcelable
import com.example.aquaminder.feature_main.data.remote.model.response.IrrigationZoneNetworkEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var colorId: Int
): Parcelable

fun IrrigationZoneDomainModel.toNetworkEntity() = IrrigationZoneNetworkEntity(
    uuid = uuid,
    name = name,
    logoId = logoId,
    colorId = colorId
)