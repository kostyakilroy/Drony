package ru.kostyakilroy.drony.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HourlyWeather(
    @PrimaryKey
    val id: Int = 0,
    val startTime: Long,
    val temperature: Double,
    @ColumnInfo(name = "precipitation")
    val precipitationProbability: Int,
    @ColumnInfo(name = "wind")
    val windSpeed: Double,
    val windGust: Double,
    @ColumnInfo(name = "clouds")
    val cloudCover: Double,
    val visibility: Double,
    val weatherCode: Int
)
