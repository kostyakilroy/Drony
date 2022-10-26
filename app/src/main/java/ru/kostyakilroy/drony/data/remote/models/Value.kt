package ru.kostyakilroy.drony.data.remote.models


import com.squareup.moshi.Json

data class Value(
    @field:Json(name = "cloudCover")
    val cloudCover: Double,
    @field:Json(name = "precipitationProbability")
    val precipitationProbability: Int,
    @field:Json(name = "temperature")
    val temperature: Double,
    @field:Json(name = "visibility")
    val visibility: Double,
    @field:Json(name = "windGust")
    val windGust: Double,
    @field:Json(name = "windSpeed")
    val windSpeed: Double,
    @field:Json(name = "weatherCode")
    val weatherCode: Int
)