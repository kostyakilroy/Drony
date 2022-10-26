package ru.kostyakilroy.drony.domain.usecases

import ru.kostyakilroy.drony.domain.drone.FlyConditions
import ru.kostyakilroy.drony.domain.drone.Quadcopter
import ru.kostyakilroy.drony.domain.weather.Weather
import ru.kostyakilroy.drony.domain.drone.FlyConditionsCode

// Высчтивает на сколько полученные данные подходят для полёта

class GetFlyConditionsUseCase {
    fun execute(weather: Weather?, quadcopter: Quadcopter): FlyConditions {
        return if (weather == null) {
            FlyConditions()
        } else {
            FlyConditions(
                temperature = isTemperatureGood(weather, quadcopter),
                precipitation = isPrecipitationGood(weather),
                wind = isWindGood(weather, quadcopter),
                windGust = isWindGustGood(weather, quadcopter),
                clouds = isCloudsGood(weather),
                visibility = isVisibilityGood(weather)
            )
        }
    }

}

private fun isTemperatureGood(weather: Weather, quadcopter: Quadcopter): FlyConditionsCode {
    return when {
        weather.temperature < quadcopter.minTemp || weather.temperature > quadcopter.maxTemp -> FlyConditionsCode.DANGER
        weather.temperature < quadcopter.minTemp * 0.7 || weather.temperature > quadcopter.maxTemp * 0.7 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}

private fun isPrecipitationGood(weather: Weather): FlyConditionsCode {
    return when {
        weather.precipitation > 50 -> FlyConditionsCode.DANGER
        weather.precipitation > 0 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}

private fun isWindGood(weather: Weather, quadcopter: Quadcopter): FlyConditionsCode {
    return when {
        weather.windSpeed > quadcopter.maxWindSpeed -> FlyConditionsCode.DANGER
        weather.windSpeed > quadcopter.maxWindSpeed * 0.7 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}

private fun isWindGustGood(weather: Weather, quadcopter: Quadcopter): FlyConditionsCode {
    return when {
        weather.windGust > quadcopter.maxWindSpeed -> FlyConditionsCode.DANGER
        weather.windGust > quadcopter.maxWindSpeed * 0.7 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}

private fun isCloudsGood(weather: Weather): FlyConditionsCode {
    return when {

        weather.cloudCover > 99.0 -> FlyConditionsCode.DANGER
        weather.cloudCover > 80.0 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}

private fun isVisibilityGood(weather: Weather): FlyConditionsCode {
    return when {
        weather.visibility < 1.2 -> FlyConditionsCode.DANGER
        weather.visibility < 3.2 -> FlyConditionsCode.ATTENTION
        else -> FlyConditionsCode.NORMAL
    }
}