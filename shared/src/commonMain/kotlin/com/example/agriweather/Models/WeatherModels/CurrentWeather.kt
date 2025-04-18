package com.example.agriweather.Models.WeatherModels

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val interval: Double,
    val is_day: Double,
    val temperature: Double,
    val time: String,
    val weathercode: Double,
    val winddirection: Double,
    val windspeed: Double
)