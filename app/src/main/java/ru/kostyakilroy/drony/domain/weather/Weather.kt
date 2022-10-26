package ru.kostyakilroy.drony.domain.weather

data class Weather(
    val startTime: Long,
    val temperature: Double,
    val precipitation: Int,
    val windSpeed: Double,
    val windGust: Double,
    val cloudCover: Double,
    val visibility: Double,
    val weatherType: WeatherType
)
