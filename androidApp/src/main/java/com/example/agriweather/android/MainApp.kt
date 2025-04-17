package com.example.agriweather.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MainApp : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin {

            androidContext(this@MainApp)
        }
    }
}