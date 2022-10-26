package ru.kostyakilroy.drony.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HourlyWeather::class], version = 1)
abstract class HourlyWeatherDatabase : RoomDatabase() {

    abstract fun hourlyWeatherDao(): HourlyWeatherDao

}