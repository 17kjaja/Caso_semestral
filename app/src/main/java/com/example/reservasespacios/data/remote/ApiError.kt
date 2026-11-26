package com.example.reservasespacios.data.remote

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException

/** Convierte cualquier fallo de red/servidor en un texto que la UI puede mostrar. */
object ApiError {

    fun mensaje(e: Throwable): String = when (e) {
        is HttpException -> {
            val cuerpo = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            val delServidor = try {
                if (cuerpo.isNullOrBlank()) null
                else JsonParser.parseString(cuerpo).asJsonObject.get("message")?.asString
            } catch (_: Exception) { null }
            delServidor ?: "Error del servidor (${e.code()})"
        }
        is IOException -> "No se pudo conectar con el servidor. Revisa tu conexión."
        else -> e.message ?: "Error inesperado"
    }
}
