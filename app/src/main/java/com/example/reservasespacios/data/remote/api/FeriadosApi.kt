package com.example.reservasespacios.data.remote.api

import com.example.reservasespacios.data.remote.dto.FeriadoDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API EXTERNA pública (no es nuestra, no tiene API key): Nager.Date.
 * Se usa para avisar al solicitante que una fecha es feriado en Chile.
 * Es independiente de los microservicios propios: otra URL base y otro Retrofit.
 */
interface FeriadosApi {
    @GET("api/v3/PublicHolidays/{anio}/CL")
    suspend fun feriados(@Path("anio") anio: Int): List<FeriadoDto>
}
