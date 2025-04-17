package com.example.agriweather.Local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.agriweather.Models.CurrentWeatherResponse


@Database(entities = [CurrentWeatherResponse::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}


fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}