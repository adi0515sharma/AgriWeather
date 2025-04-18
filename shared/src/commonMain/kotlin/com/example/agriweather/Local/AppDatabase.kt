package com.example.agriweather.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.agriweather.Models.WeatherModels.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


@Database(entities = [CurrentWeatherResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}


fun getRoomDatabase(builder : RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .addMigrations()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}


