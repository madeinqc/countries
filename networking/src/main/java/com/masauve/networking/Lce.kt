package com.masauve.networking

sealed class Lce<T> {
    class Loading<T> : Lce<T>()
    data class Content<T>(val packet: T) : Lce<T>()
    data class Error<T>(val packet: T?, val error: Throwable?) : Lce<T>()

    fun valueOrNull(): T? =
        when (this) {
            is Loading -> null
            is Content -> packet
            is Error -> packet
        }

    fun isLoading(): Boolean =
        when (this) {
            is Loading -> true
            is Content -> false
            is Error -> false
        }

    fun errorOrNull(): Throwable? =
        when (this) {
            is Loading -> null
            is Content -> null
            is Error -> error
        }
}
