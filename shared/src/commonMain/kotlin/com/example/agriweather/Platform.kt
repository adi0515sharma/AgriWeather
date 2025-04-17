package com.example.agriweather

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform