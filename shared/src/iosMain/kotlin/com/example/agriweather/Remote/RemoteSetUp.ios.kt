package com.example.agriweather.Remote

import io.ktor.client.HttpClient

actual fun getHttpClient(): HttpClient {
    return HttpClient()
}