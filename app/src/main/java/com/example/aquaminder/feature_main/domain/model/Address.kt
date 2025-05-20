package com.example.aquaminder.feature_main.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val street: String = "",
    val number: String = "",
    val postalCode: String = "",
    val city: String = "",
    val country: String = ""
): Parcelable

fun Address.toSingleString(): String {
    return "$street $number, $postalCode $city, $country"
}

fun stringToAddress(addressString: String): Address {
    try {
        val parts = addressString.split(",").map { it.trim() }
        if (parts.size < 3) return Address()

        // First part: "street number"
        val streetAndNumber = parts[0].split(" ")
        val number = streetAndNumber.lastOrNull()?.takeIf { it.all { char -> char.isDigit() } } ?: ""
        val street = streetAndNumber.dropLast(1).joinToString(" ")

        // Second part: "postalCode city"
        val postalAndCity = parts[1].split(" ")
        val postalCode = postalAndCity.firstOrNull().orEmpty()
        val city = postalAndCity.drop(1).joinToString(" ")

        // Third part: "country"
        val country = parts[2]

        return Address(
            street = street,
            number = number,
            postalCode = postalCode,
            city = city,
            country = country
        )
    } catch (e: Exception) {
        return Address()
    }
}