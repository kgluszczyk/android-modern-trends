package com.modern.commons.utils

sealed class Resource<out DataType, out ErrorType>

val <T, R> Resource<T, R>.data
    get() = if (this is ResourceSuccess<T>) this.data else null

object ResourceLoading : Resource<Nothing, Nothing>()

data class ResourceSuccess<DataType>(val data: DataType) : Resource<DataType, Nothing>()

data class ResourceError<ErrorType>(val error: ErrorType) : Resource<Nothing, ErrorType>()