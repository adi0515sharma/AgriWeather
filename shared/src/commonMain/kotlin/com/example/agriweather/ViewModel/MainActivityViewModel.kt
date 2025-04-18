package com.example.agriweather.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agriweather.Models.WeatherModels.CurrentWeatherResponse
import com.example.agriweather.Repository.RemoteRepo
import com.example.agriweather.Repository.WeatherRepo
import com.example.agriweather.Repository.checkIsGPSON
import com.example.agriweather.Repository.checkLocationPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


class MainActivityViewModel(
    private val remoteRepo: RemoteRepo,
    private val weatherRepo: WeatherRepo
) : ViewModel() {


    private val _uiState = MutableStateFlow<CurrentWeatherResponse?>(null)
    val uiState: StateFlow<CurrentWeatherResponse?> = _uiState

    private val _askLocationPermission = MutableStateFlow<Boolean>(false)
    val askLocationPermission: StateFlow<Boolean> = _askLocationPermission

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    init {
        viewModelScope.launch(Dispatchers.IO){
            weatherRepo.getWeather().collectLatest {
                _uiState.value = it
            }
        }

        viewModelScope.launch {
            fetchCurrentWeather()
        }

    }

    suspend fun fetchCurrentWeather() {


        if(!checkLocationPermission()){
            changeLocationAsk(true)
            return
        }


        _isLoading.value = true

        _errorMessage.value = null
        val errorMessage = remoteRepo.getCurrentWeather()
        _errorMessage.value = errorMessage
        _isLoading.value = false
    }

    fun changeLocationAsk(shouldAsk  : Boolean){
        _askLocationPermission.value = shouldAsk

    }
}