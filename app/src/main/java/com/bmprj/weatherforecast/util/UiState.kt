package com.bmprj.weatherforecast.util

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    class Success<T>(val result: T) : UiState<T>()
    class Error(val error: Throwable) : UiState<Nothing>()
}