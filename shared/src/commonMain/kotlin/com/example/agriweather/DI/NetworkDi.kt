package com.example.agriweather.DI

import com.example.agriweather.Local.AppDatabase
import com.example.agriweather.Local.getRoomDatabase
import com.example.agriweather.Remote.getHttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val apiClient = module {
    factory {
        getHttpClient().config {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}