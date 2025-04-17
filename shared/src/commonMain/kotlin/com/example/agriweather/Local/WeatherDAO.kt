package com.example.agriweather.Local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agriweather.Models.CurrentWeatherResponse

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: CurrentWeatherResponse)

    @Query("SELECT * FROM weather LIMIT 1")
    suspend fun getWeather(): CurrentWeatherResponse?

    @Query("DELETE FROM weather")
    suspend fun clearAll()
}