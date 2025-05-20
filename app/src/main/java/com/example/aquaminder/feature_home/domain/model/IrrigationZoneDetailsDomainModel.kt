package com.example.aquaminder.feature_home.domain.model

import android.os.Parcelable
import com.example.aquaminder.feature_main.domain.model.Address
import kotlinx.parcelize.Parcelize

@Parcelize
data class IrrigationZoneDetailsDomainModel(
    val uuid: String,
    val name: String,
    var logoId: Int,
    var address: Address,
    var valves: List<ValveDomainModel>
): Parcelable
