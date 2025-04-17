package com.example.agriweather.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agriweather.Models.CurrentWeather
import com.example.agriweather.Models.CurrentWeatherResponse
import com.example.agriweather.Remote.createHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


class MainActivityViewModel : ViewModel() {


    private val _uiState = MutableStateFlow<CurrentWeatherResponse?>(null)
    val uiState: StateFlow<CurrentWeatherResponse?> = _uiState


    init {
        viewModelScope.launch(Dispatchers.IO){
            _uiState.value = fetchSomething()
        }
    }

    suspend fun fetchSomething(): CurrentWeatherResponse {
        val client = createHttpClient()
        val response: CurrentWeatherResponse = client.get("https://api.open-meteo.com/v1/forecast?latitude=19.02&longitude=72.85&current_weather=true").body()
        return response
    }
}