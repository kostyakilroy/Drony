package ru.kostyakilroy.drony.data.mappers

import ru.kostyakilroy.drony.data.local.HourlyWeather
import ru.kostyakilroy.drony.data.remote.models.Interval
import ru.kostyakilroy.drony.data.remote.models.Timeline
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.domain.weather.WeatherType
import java.time.ZonedDateTime

fun HourlyWeather.toWeather(): Weather {
    return Weather(
        startTime = startTime,
        temperature = temperature,
        precipitation = precipitationProbability,
        windSpeed = windSpeed,
        windGust = windGust,
        cloudCover = cloudCover,
        visibility = visibility,
        weatherType = WeatherType.fromCodeToWeatherType(weatherCode)
    )
}

fun Timeline.toHourlyWeatherList(): List<HourlyWeather> {
    return this.intervals.mapIndexed { index, interval ->
        HourlyWeather(
            id = index,
            startTime = ZonedDateTime.parse(interval.startTime).toEpochSecond(),
            temperature = interval.weather.temperature,
            precipitationProbability = interval.weather.precipitationProbability,
            windSpeed = interval.weather.windSpeed,
            windGust = interval.weather.windGust,
            cloudCover = interval.weather.cloudCover,
            visibility = interval.weather.visibility,
            weatherCode = interval.weather.weatherCode
        )
    }
}

fun Interval.toHourlyWeather(): HourlyWeather {
    return HourlyWeather (
        startTime = ZonedDateTime.parse(this.startTime).toEpochSecond(),
        temperature = this.weather.temperature,
        precipitationProbability = this.weather.precipitationProbability,
        windSpeed = this.weather.windSpeed,
        windGust = this.weather.windGust,
        cloudCover = this.weather.cloudCover,
        visibility = this.weather.visibility,
        weatherCode = this.weather.weatherCode
    )
}