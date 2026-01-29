package com.codehong.app.kplay.domain.model

import java.io.IOException

sealed class CallStatus<out T> {
    object Uninitialized : CallStatus<Nothing>()

    object Loading : CallStatus<Nothing>()

    object Empty : CallStatus<Nothing>()

    data class Success<T>(val responseData: T) : CallStatus<T>()

    data class Error(val exception: Throwable?) : CallStatus<Nothing>() {
        val isNetworkError = exception is IOException
    }

    data class ErrorWithData<T>(val exception: Throwable? = null, val errorData: T) : CallStatus<T>() {
        val isNetworkError = exception is IOException
    }

    data class AnyCase(val caseId: Int): CallStatus<Nothing>()
}