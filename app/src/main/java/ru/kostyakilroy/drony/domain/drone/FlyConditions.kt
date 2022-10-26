package ru.kostyakilroy.drony.domain.drone

data class FlyConditions(
    val temperature: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val precipitation: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val wind: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val windGust: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val clouds: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val visibility: FlyConditionsCode = FlyConditionsCode.NORMAL,
)
