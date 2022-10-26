package ru.kostyakilroy.drony.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kostyakilroy.drony.data.local.HourlyWeather
import ru.kostyakilroy.drony.data.local.HourlyWeatherDao
import ru.kostyakilroy.drony.data.mappers.toHourlyWeatherList
import ru.kostyakilroy.drony.data.mappers.toWeather
import ru.kostyakilroy.drony.data.remote.WeatherAPI
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.domain.repository.WeatherRepository
import ru.kostyakilroy.drony.domain.util.Resource
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherAPI,
    private val dao: HourlyWeatherDao
) : WeatherRepository {

    override fun getHourlyWeather(
        latitude: Double,
        longitude: Double
    ): Flow<Resource<List<Weather>>> = flow {

        val hourlyWeather = dao.loadAllHourlyWeather()

        if (hourlyWeather.isNotEmpty()) emit(Resource.Loading(data = hourlyWeather.map { it.toWeather() }))

        if (isReasonToInsert(hourlyWeather)) {
            try {
                val locationString = "$latitude, $longitude"
                val remoteHourlyWeather = api.getWeatherData(locationString)

                val mappedHourlyWeather: List<HourlyWeather> = remoteHourlyWeather.data.timelines.first().toHourlyWeatherList()

                // todo: is necessary?
                if (hourlyWeather.isNotEmpty()) {
                    dao.deleteAll()
                }
                dao.insertHourlyWeather(mappedHourlyWeather)

                emit(Resource.Success(data = mappedHourlyWeather.map { it.toWeather() }))

            } catch (e: Exception) {
                emit(Resource.Error(
                    message = e.message
                ))
            }
        }

        val newHourlyWeather = dao.loadAllHourlyWeather()
        if (newHourlyWeather.isNotEmpty()) emit(Resource.Success(data = newHourlyWeather.map { it.toWeather() }))
    }

    private fun isReasonToInsert(weatherList: List<HourlyWeather>): Boolean {
        if (weatherList.isEmpty()) return true
        val currentMoment = Instant.now().epochSecond
        val secondHourInHourlyWeather = weatherList[1].startTime
        return currentMoment > secondHourInHourlyWeather
    }

}