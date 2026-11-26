package com.example.reservasespacios.data.remote.api

import com.example.reservasespacios.data.remote.dto.*
import retrofit2.http.*

/** Cliente de usuarios-service (:8081) */
interface UsuarioApi {
    @POST("api/usuarios/login")
    suspend fun login(@Body body: LoginRequest): UsuarioDto

    @GET("api/usuarios")
    suspend fun listar(): List<UsuarioDto>

    @POST("api/usuarios")
    suspend fun crear(@Body usuario: UsuarioDto): UsuarioDto

    @PUT("api/usuarios/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body usuario: UsuarioDto): UsuarioDto

    @DELETE("api/usuarios/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}

/** Cliente de espacios-service (:8082): espacios y bloqueos */
interface EspacioApi {
    @GET("api/espacios")
    suspend fun listar(): List<EspacioDto>

    @GET("api/espacios/{id}")
    suspend fun obtener(@Path("id") id: Int): EspacioDto

    @POST("api/espacios")
    suspend fun crear(@Body espacio: EspacioDto): EspacioDto

    @PUT("api/espacios/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body espacio: EspacioDto): EspacioDto

    @DELETE("api/espacios/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}

interface BloqueoApi {
    @GET("api/bloqueos")
    suspend fun listar(): List<BloqueoDto>

    @GET("api/bloqueos/traslape")
    suspend fun hayTraslape(
        @Query("espacioId") espacioId: Int,
        @Query("fecha") fecha: String,
        @Query("inicio") inicio: String,
        @Query("fin") fin: String
    ): Boolean

    @POST("api/bloqueos")
    suspend fun crear(@Body bloqueo: BloqueoRequest): BloqueoDto

    @DELETE("api/bloqueos/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}

/** Cliente de reservas-service (:8083): reservas y notificaciones */
interface ReservaApi {
    @GET("api/reservas")
    suspend fun listar(@Query("estado") estado: String? = null): List<ReservaDto>

    @GET("api/reservas/usuario/{usuarioId}")
    suspend fun porUsuario(@Path("usuarioId") usuarioId: Int): List<ReservaDto>

    @GET("api/reservas/traslape")
    suspend fun hayTraslape(
        @Query("espacioId") espacioId: Int,
        @Query("fecha") fecha: String,
        @Query("inicio") inicio: String,
        @Query("fin") fin: String
    ): Boolean

    @POST("api/reservas")
    suspend fun crear(@Body reserva: ReservaDto): ReservaDto

    @PUT("api/reservas/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body reserva: ReservaDto): ReservaDto

    @PUT("api/reservas/{id}/resolver")
    suspend fun resolver(@Path("id") id: Int, @Body body: ResolverRequest): ReservaDto

    @PUT("api/reservas/{id}/cancelar")
    suspend fun cancelar(@Path("id") id: Int): ReservaDto

    @DELETE("api/reservas/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}

interface NotificacionApi {
    @GET("api/notificaciones/usuario/{usuarioId}")
    suspend fun porUsuario(@Path("usuarioId") usuarioId: Int): List<NotificacionDto>

    @GET("api/notificaciones/usuario/{usuarioId}/no-leidas")
    suspend fun contarNoLeidas(@Path("usuarioId") usuarioId: Int): Long

    @POST("api/notificaciones")
    suspend fun crear(@Body notificacion: NotificacionDto): NotificacionDto

    @PUT("api/notificaciones/usuario/{usuarioId}/leidas")
    suspend fun marcarTodasLeidas(@Path("usuarioId") usuarioId: Int)

    @DELETE("api/notificaciones/{id}")
    suspend fun eliminar(@Path("id") id: Int)
}
