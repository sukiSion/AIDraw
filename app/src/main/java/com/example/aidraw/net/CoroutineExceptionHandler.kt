package com.example.aidraw.net

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class AppCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("ExceptionHandler", exception.stackTraceToString())
    }
}