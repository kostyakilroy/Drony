package ru.kostyakilroy.drony.domain.drone

data class Quadcopter(
    val name: Int,
    val maxSpeed: Double,
    val maxWindSpeed: Double,
    val minTemp: Double,
    val maxTemp: Double
)
