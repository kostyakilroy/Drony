package ru.kostyakilroy.drony.domain.drone

import ru.kostyakilroy.drony.R

enum class FlyConditionsCode(val color: Int) {
    NORMAL(R.color.normal),
    ATTENTION(R.color.attention),
    DANGER(R.color.danger)
}