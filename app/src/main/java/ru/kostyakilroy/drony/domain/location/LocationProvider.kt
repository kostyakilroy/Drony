package ru.kostyakilroy.drony.domain.location

import ru.kostyakilroy.drony.domain.util.Resource

interface LocationProvider {
    suspend fun getCurrentLocation(): Resource<SimpleLocation>
}