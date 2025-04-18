package com.example.agriweather.DI

import com.example.agriweather.Repository.RemoteRepo
import com.example.agriweather.Repository.WeatherRepo
import com.example.agriweather.ViewModel.MainActivityViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelDI = module {
    viewModel { MainActivityViewModel(get(), get()) }
}