package com.example.aquaminder.feature_main.utils

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import com.example.aquaminder.feature_main.domain.model.Address
import java.util.Locale

object AddressUtils {

    fun getAddressModelByCoordinates(context: Context, lat: Double, lon: Double): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)

        addresses?.firstOrNull()?.let { address ->
            val street = address.thoroughfare.orEmpty()
            val number = address.subThoroughfare.orEmpty()
            val postalCode = address.postalCode.orEmpty()
            val city = address.locality.orEmpty()
            val country = address.countryName.orEmpty()
            val addressModel = Address(
                street,
                number,
                postalCode,
                city,
                country
            )
            return addressModel
        } ?: run {
            return null
        }
    }


}