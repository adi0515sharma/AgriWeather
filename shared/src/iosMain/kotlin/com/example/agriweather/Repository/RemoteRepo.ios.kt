package com.example.agriweather.Repository

import com.example.agriweather.Models.UserLocation

actual suspend fun getCurrentLocation(): UserLocation? {
    TODO("Not yet implemented")
}

actual suspend fun checkLocationPermission(): Boolean {
    TODO("Not yet implemented")
}

actual fun getLocalTime(time: String): String {
    TODO("Not yet implemented")
}

actual fun getLocationName(lat: Long, long: Long): String {
    TODO("Not yet implemented")
}