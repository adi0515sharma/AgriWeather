package com.example.agriweather.Models.WeatherModels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "weather")
@Serializable
data class CurrentWeatherResponse(
    @PrimaryKey val id: Int = 0,
    @Embedded(prefix = "units_") val current_weather: CurrentWeather,
    @Embedded(prefix = "current_") val current_weather_units: CurrentWeatherUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Double
)