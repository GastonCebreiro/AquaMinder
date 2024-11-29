package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_home.data.model.IrrigationZoneDetailsNetworkEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneDetailsDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var colorId: Int
): Parcelable

fun IrrigationZoneDetailsDomainModel.toNetworkEntity() = IrrigationZoneDetailsNetworkEntity(
    uuid = uuid,
    name = name,
    logoId = logoId,
    colorId = colorId
)