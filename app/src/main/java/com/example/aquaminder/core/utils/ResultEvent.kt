package com.example.aquaminder.core.utils

sealed class ResultEvent<out T> {
    data class Success<out T>(val data: T) : ResultEvent<T>()
    data class Error(val error: AppError) : ResultEvent<Nothing>()
}

//  todo gc val error: AppError para identificar tipos de errores