package ru.kostyakilroy.drony.domain.drone

import ru.kostyakilroy.drony.R
import ru.kostyakilroy.drony.domain.drone.Quadcopter

// Набор заранее известных дронов

enum class DJIQuadcopters(val quadcopter: Quadcopter) {
    AIR2S(Quadcopter(name = R.string.djiAir2s, maxSpeed = 19.0, maxWindSpeed = 10.7, minTemp = 0.0, maxTemp = 40.0)),
    MINI2(Quadcopter(name = R.string.djiMini2, maxSpeed = 16.0, maxWindSpeed = 1.5, minTemp = 0.0, maxTemp = 40.0)),//todo wind speed 8.5
    MAVIC3(Quadcopter(name = R.string.djiMavic3, maxSpeed = 19.0, maxWindSpeed = 12.0, minTemp = -10.0, maxTemp = 40.0))
}