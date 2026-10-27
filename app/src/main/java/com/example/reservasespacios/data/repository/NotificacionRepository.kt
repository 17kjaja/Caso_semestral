package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.NotificacionDao
import com.example.reservasespacios.data.local.NotificacionEntity
import kotlinx.coroutines.flow.Flow

class NotificacionRepository(private val dao: NotificacionDao) {

    fun obtenerPorUsuario(usuarioId: Int): Flow<List<NotificacionEntity>> =
        dao.obtenerPorUsuario(usuarioId)

    fun contarNoLeidas(usuarioId: Int): Flow<Int> = dao.contarNoLeidas(usuarioId)

    suspend fun crear(notificacion: NotificacionEntity) = dao.insertar(notificacion)

    suspend fun marcarTodasLeidas(usuarioId: Int) = dao.marcarTodasLeidas(usuarioId)
}