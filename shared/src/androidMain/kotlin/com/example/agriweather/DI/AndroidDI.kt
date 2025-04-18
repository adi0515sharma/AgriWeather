package com.example.agriweather.DI

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agriweather.Local.AppDatabase
import org.koin.dsl.module


val androidModule = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val context  : Context = get()
        val dbFile = context.getDatabasePath("weather.db")

        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbFile.absolutePath
        )
    }
}
