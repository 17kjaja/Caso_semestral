package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.NotificacionDao
import com.example.reservasespacios.data.local.NotificacionEntity
import com.example.reservasespacios.data.remote.RetrofitClient
import com.example.reservasespacios.data.remote.api.NotificacionApi
import com.example.reservasespacios.data.remote.dto.NotificacionDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class NotificacionRepository(
    private val dao: NotificacionDao,
    private val api: NotificacionApi = RetrofitClient.notificacionApi
) {
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<NotificacionEntity>> =
        dao.obtenerPorUsuario(usuarioId).onStart {
            sincronizar(usuarioId)
        }

    fun contarNoLeidas(usuarioId: Int): Flow<Int> = dao.contarNoLeidas(usuarioId)

    suspend fun sincronizar(usuarioId: Int) {
        try {
            api.porUsuario(usuarioId).forEach { dto ->
                dao.insertar(
                    NotificacionEntity(
                        id = dto.id ?: 0,
                        usuarioId = dto.usuarioId,
                        titulo = dto.titulo,
                        mensaje = dto.mensaje,
                        leida = dto.leida,
                        fecha = dto.fecha ?: ""
                    )
                )
            }
        } catch (_: Exception) { }
    }

    suspend fun crear(notificacion: NotificacionEntity) {
        try {
            val creada = api.crear(
                NotificacionDto(
                    usuarioId = notificacion.usuarioId,
                    titulo = notificacion.titulo,
                    mensaje = notificacion.mensaje,
                    leida = notificacion.leida,
                    fecha = notificacion.fecha
                )
            )
            dao.insertar(notificacion.copy(id = creada.id ?: 0))
        } catch (_: Exception) {
            dao.insertar(notificacion)
        }
    }

    suspend fun marcarTodasLeidas(usuarioId: Int) {
        try {
            api.marcarTodasLeidas(usuarioId)
        } catch (_: Exception) { }
        dao.marcarTodasLeidas(usuarioId)
    }
}