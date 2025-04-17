package com.example.agriweather.Models

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeather(
    val interval: Int,
    val is_day: Int,
    val temperature: Double,
    val time: String,
    val weathercode: Int,
    val winddirection: Int,
    val windspeed: Double
)