package com.modern.android.forms

import retrofit2.HttpException

fun wrapUnauthorizedErrorIfNeeded(throwable: Throwable): Throwable {
    return if (throwable is HttpException) {
        when(throwable.code()) {
            401 -> throwable
            403 -> throwable
            424 -> throwable
            else -> throwable
        }
    } else throwable
}