package com.example.aquaminder.feature_main.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_main.data.remote.model.IrrigationZoneNetworkEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var colorId: Int,
    var location: Location
): Parcelable

fun IrrigationZoneDomainModel.toNetworkEntity() = IrrigationZoneNetworkEntity(
    id = uuid,
    name = name,
    logoId = logoId,
    latitude = location.latitude,
    longitude = location.longitude,
    address = location.address.toSingleString(),
)