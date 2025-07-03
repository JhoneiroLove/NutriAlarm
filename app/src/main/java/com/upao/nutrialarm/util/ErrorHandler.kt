package com.upao.nutrialarm.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ErrorHandler {

    private const val TAG = "ErrorHandler"

    fun createErrorHandler(
        onError: (Throwable) -> Unit = { logError(it) }
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            logError(exception)
            onError(exception)
        }
    }

    fun logError(throwable: Throwable) {
        Log.e(TAG, "Error: ${throwable.message}", throwable)
    }

    fun getUserFriendlyMessage(throwable: Throwable): String {
        return when (throwable) {
            is java.net.ConnectException -> "Error de conexión. Verifica tu internet."
            is java.net.SocketTimeoutException -> "La conexión tardó demasiado. Inténtalo de nuevo."
            is IllegalStateException -> "Error en el estado de la aplicación. Reinicia la app."
            is NullPointerException -> "Error interno. Por favor contacta soporte."
            is SecurityException -> "Permisos insuficientes. Verifica la configuración."
            else -> "Ha ocurrido un error inesperado. Inténtalo de nuevo."
        }
    }

    @Composable
    fun HandleError(
        error: Throwable?,
        onDismiss: () -> Unit
    ) {
        LaunchedEffect(error) {
            error?.let {
                logError(it)
            }
        }
    }
}

// Extensión para CoroutineScope
fun CoroutineScope.launchWithErrorHandling(
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
) {
    launch(ErrorHandler.createErrorHandler(onError)) {
        block()
    }
}