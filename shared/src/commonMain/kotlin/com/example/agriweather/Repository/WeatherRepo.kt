package com.example.agriweather.Repository

import com.example.agriweather.Local.WeatherDao
import com.example.agriweather.Models.WeatherModels.CurrentWeatherResponse

class WeatherRepo (val weatherDao: WeatherDao) {

    fun getWeather() = weatherDao.getWeather()

    suspend fun clearWeather() = weatherDao.clearAll()

    suspend fun insertNewRecord(weatherResponse: CurrentWeatherResponse) = weatherDao.insertWeather(weatherResponse)
}