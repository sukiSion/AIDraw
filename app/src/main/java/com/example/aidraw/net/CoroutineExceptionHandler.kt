package com.example.aidraw.net

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class AppCoroutineExceptionHandler(
    val onError:(context: CoroutineContext , exception: Throwable) -> Unit
) : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("ExceptionHandler", exception.stackTraceToString())
        onError(context , exception)
    }
}