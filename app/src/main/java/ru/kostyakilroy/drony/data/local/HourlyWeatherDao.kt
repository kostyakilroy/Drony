package ru.kostyakilroy.drony.data.local

import androidx.room.*

@Dao
interface HourlyWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeather(hourlyWeather: List<HourlyWeather>)

    @Update
    suspend fun updateOldHourlyWeather(hourlyWeather: List<HourlyWeather>)

    @Query("SELECT * FROM hourlyweather")
    suspend fun loadAllHourlyWeather(): List<HourlyWeather>

    @Query("DELETE FROM hourlyweather")
    suspend fun deleteAll()

    @Query("UPDATE hourlyweather SET id = 1 WHERE id = 1")
    suspend fun updatePrimaryKey()

}