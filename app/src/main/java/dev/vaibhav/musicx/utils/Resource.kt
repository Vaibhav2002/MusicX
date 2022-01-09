package dev.vaibhav.musicx.utils

sealed class Resource<T>(
    open val data: T? = null,
    open val message: String = "",
    open val errorType: ErrorType = ErrorType.UNKNOWN
) {

    class Loading<T>() : Resource<T>()

    data class Success<T>(override val data: T?, override val message: String = "") :
        Resource<T>(data, message)

    data class Error<T>(
        override val errorType: ErrorType = ErrorType.UNKNOWN,
        override val message: String = errorType.errorMessage
    ) : Resource<T>(null, message, errorType)
}

enum class ErrorType(val errorMessage: String) {
    NO_INTERNET("Looks like you don't have an internet connection"),
    UNKNOWN("Oops something went wrong")
}
