package ru.kostyakilroy.drony.presentation.state

import ru.kostyakilroy.drony.domain.drone.FlyConditions
import ru.kostyakilroy.drony.domain.weather.Weather


data class WeatherUiState (
    val weatherInfo: List<Weather>? = null,
    val flyConditions: FlyConditions? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
