package ru.kostyakilroy.drony.presentation.state

import ru.kostyakilroy.drony.domain.drone.FlyConditions

data class FlyConditionsUiState (
    val flyConditions: FlyConditions? = null
)