package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.remote.api.FeriadosApi
import com.example.reservasespacios.data.remote.dto.FeriadoDto

class FeriadosRepository(private val api: FeriadosApi) {

    /** Feriados de Chile del año indicado. */
    suspend fun feriados(anio: Int): List<FeriadoDto> = api.feriados(anio)

    /** Devuelve el feriado si la fecha (AAAA-MM-DD) coincide con alguno, o null. */
    suspend fun feriadoDe(fecha: String): FeriadoDto? {
        val anio = fecha.take(4).toIntOrNull() ?: return null
        return api.feriados(anio).firstOrNull { it.date == fecha }
    }
}
