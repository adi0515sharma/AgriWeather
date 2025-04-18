package com.example.agriweather.android

import android.app.Application
import android.content.Context
import com.example.agriweather.DI.androidModule

import com.example.agriweather.DI.apiClient
import com.example.agriweather.DI.commonModule
import com.example.agriweather.DI.repoDi
import com.example.agriweather.DI.viewModelDI

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MainApp : Application(), KoinComponent {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            modules(androidModule, apiClient, commonModule)
            modules(repoDi,viewModelDI)
        }
    }
}