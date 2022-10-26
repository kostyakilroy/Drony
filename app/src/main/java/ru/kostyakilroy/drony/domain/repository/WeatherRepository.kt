package ru.kostyakilroy.drony.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.domain.util.Resource

interface WeatherRepository {
    fun getHourlyWeather(latitude: Double, longitude: Double): Flow<Resource<List<Weather>>>
}