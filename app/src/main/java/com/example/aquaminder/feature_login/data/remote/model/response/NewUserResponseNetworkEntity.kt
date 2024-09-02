package com.example.aquaminder.feature_login.data.remote.model.response

import com.example.aquaminder.feature_login.domain.model.response.NewUserResponseDomainModel
import com.google.gson.annotations.SerializedName

data class NewUserResponseNetworkEntity(
    @SerializedName("status")
    val status: Int? = null
)

fun NewUserResponseNetworkEntity.toDomainModel() = NewUserResponseDomainModel(
        status = status ?: -1
)