package ru.kostyakilroy.drony.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kostyakilroy.drony.BuildConfig
import ru.kostyakilroy.drony.data.remote.models.WeatherDto

private const val apikey = BuildConfig.API_KEY

interface WeatherAPI {

    @GET("timelines?&fields=temperature" +
            "&fields=windSpeed" +
            "&fields=windGust" +
            "&fields=weatherCode" +
            "&fields=cloudCover" +
            "&fields=visibility" +
            "&fields=precipitationProbability" +
            "&fields=humidity" +
            "&fields=weatherCode" +
            "&units=metric" +
            "&timesteps=1h&startTime=now&endTime=nowPlus24h&timezone=Asia%2FYekaterinburg" + //TODO Timezone
            "&apikey=$apikey")
    suspend fun getWeatherData(@Query("location") loc: String): WeatherDto
}