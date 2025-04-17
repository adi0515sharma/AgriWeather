package com.example.agriweather.Models

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val current_weather: CurrentWeather,
    val current_weather_units: CurrentWeatherUnits,
    val elevation: Int,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)