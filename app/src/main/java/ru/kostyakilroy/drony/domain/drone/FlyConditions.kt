package ru.kostyakilroy.drony.domain.drone

// Определяет в какой зоне находятся полученные значения
// Например температура подходит для полета(FlyConditionsCode.NORMAL)

data class FlyConditions(
    val temperature: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val precipitation: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val wind: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val windGust: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val clouds: FlyConditionsCode = FlyConditionsCode.NORMAL,
    val visibility: FlyConditionsCode = FlyConditionsCode.NORMAL,
)
