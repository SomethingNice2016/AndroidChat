@file:Suppress("UNCHECKED_CAST")

package ua.kucher.chat.core


fun <IN, OUT> Result<IN>.map(mapper: (IN) -> OUT): Result<OUT> {
    return if (isSuccess)
        runCatching {
            mapper(getOrThrow())
        }
    else
        Result.failure(exceptionOrNull() ?: NullPointerException("Value is null"))
}

suspend fun <IN, OUT> Result<IN>.mapAsync(mapper: suspend (IN) -> OUT): Result<OUT> {
    return if (isSuccess)
        runCatching {
            mapper(getOrThrow())
        }
    else
        Result.failure(exceptionOrNull() ?: NullPointerException("Value is null"))
}

suspend fun <IN, OUT> Result<IN>.flatMapAsync(mapper: suspend (IN) -> Result<OUT>): Result<OUT> {
    return if (isSuccess)
        try {
            mapper(getOrThrow())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    else
        Result.failure(exceptionOrNull() ?: NullPointerException("Value is null"))
}

fun <IN, OUT> Result<IN>.flatMap(mapper: (IN) -> Result<OUT>): Result<OUT> {
    return if (isSuccess)
        try {
            mapper(getOrThrow())
        } catch (t: Throwable) {
            Result.failure(t)
        }
    else
        Result.failure(exceptionOrNull() ?: NullPointerException("Value is null"))
}

suspend fun <T> Result<T>.onSuccessAsync(onSuccess: suspend (T) -> Unit): Result<T> {
    return try {
        if (isSuccess) onSuccess(getOrThrow())
        this
    } catch (t: Throwable) {
        Result.failure(t)
    }
}

fun Result<*>.toUnit(): Result<Unit> {
    return map { }
}


