package com.example.agriweather.Repository

import com.example.agriweather.Models.UserLocation
import com.example.agriweather.Models.WeatherModels.CurrentWeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import kotlinx.io.IOException


expect suspend fun getCurrentLocation(): UserLocation?
expect suspend fun checkLocationPermission(): Boolean


class RemoteRepo (val client: HttpClient, val weatherRepo: WeatherRepo) {


    suspend fun getCurrentWeather() : String?{
        try{

            val userCurrentLocation : UserLocation = getCurrentLocation() ?: return "No Location Found";
            val response: CurrentWeatherResponse = client.get("https://api.open-meteo.com/v1/forecast?latitude=${userCurrentLocation.latitude}&longitude=${userCurrentLocation.longitude}&current_weather=true").body()
            weatherRepo.clearWeather()
            weatherRepo.insertNewRecord(response)

        }
        catch (e : Exception){

             return  when (e) {
                 is IOException -> "No internet connection"
                 is ClientRequestException -> "Bad request: ${e.message}" // 4xx
                 is ServerResponseException -> "Server error: ${e.message}" // 5xx
                 is RedirectResponseException -> "Redirect error: ${e.message}" // 3xx
                 else -> "Something went wrong"
            }
        }

        return null
    }
}