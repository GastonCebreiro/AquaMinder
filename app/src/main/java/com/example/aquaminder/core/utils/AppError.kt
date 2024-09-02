package com.example.aquaminder.core.utils

sealed class AppError {
    // Generic Errors
    data class GenericError(val errorMsg: String = "") : AppError()

    // Network Errors
    object NetworkError : AppError()

    // Login Errors
    object WrongPassword : AppError()
    object UsernameNotFound : AppError()
    object InvalidUser: AppError()
    object EmptyList: AppError()

    // Register Errors
    object MailUnavailable : AppError()
    object UserNameUnavailable : AppError()

}