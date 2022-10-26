package ru.kostyakilroy.drony.domain.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<E>(message: String?, data: E? = null) : Resource<E>(data, message)
}
