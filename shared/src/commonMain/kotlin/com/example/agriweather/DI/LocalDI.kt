package com.example.agriweather.DI

import com.example.agriweather.Local.AppDatabase
import com.example.agriweather.Local.getRoomDatabase
import org.koin.dsl.module


val commonModule = module {
    single { getRoomDatabase(get()) }
    factory {
        get<AppDatabase>().getWeatherDao()
    }
}



