package com.example.agriweather.DI

import com.example.agriweather.Repository.RemoteRepo
import com.example.agriweather.Repository.WeatherRepo
import org.koin.dsl.module


val repoDi = module {
    factory { WeatherRepo(get()) }
    factory { RemoteRepo(get(), get()) }
}