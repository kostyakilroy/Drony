package ru.kostyakilroy.drony.data.remote.models

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name = "data")
    val data: Data
)

data class Data(
    @field:Json(name = "timelines")
    val timelines: List<Timeline>
)

data class Timeline(
    val endTime: String,
    val intervals: List<Interval>,
    val startTime: String,
    val timestep: String
)

data class Interval(
    @field:Json(name = "startTime")
    val startTime: String,
    @field:Json(name = "values")
    val weather: Value
)