package ru.kostyakilroy.drony.domain.usecases

import ru.kostyakilroy.drony.domain.drone.DJIQuadcopters
import ru.kostyakilroy.drony.domain.drone.Quadcopter

// Возвращает один из заранее известных дронов

class GetCurrentDroneUseCase {
    fun execute(droneName: String?): Quadcopter{
        val djiDrone = DJIQuadcopters.valueOf(droneName ?: DJIQuadcopters.AIR2S.name)
        return djiDrone.quadcopter
    }
}