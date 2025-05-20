package com.example.aquaminder.feature_home.data.model.response

import com.example.aquaminder.core.utils.AppConstants
import com.example.aquaminder.feature_home.data.model.ValveNetworkEntity
import com.example.aquaminder.feature_home.data.model.toDomainModel
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import com.example.aquaminder.feature_main.domain.model.stringToAddress
import com.google.gson.annotations.SerializedName

data class GetIrrigationZoneDetailsResponse(
    val status: Int? = null,
    @SerializedName("id")
    val uuid: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("logoId")
    var logoId: Int? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("valves")
    var valves: List<ValveNetworkEntity>? = null
)

fun GetIrrigationZoneDetailsResponse.toDomainModel() = IrrigationZoneDetailsDomainModel(
    uuid = uuid.orEmpty(),
    name = name ?: AppConstants.DEFAULT_IZ_NAME,
    logoId = logoId ?: AppConstants.DEFAULT_LOGO_ID,
    address = stringToAddress(address.orEmpty()),
    valves = valves?.map {
        it.toDomainModel()
    }.orEmpty()
)
