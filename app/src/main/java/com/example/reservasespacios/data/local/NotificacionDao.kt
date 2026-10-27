package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacionDao {

    @Insert
    suspend fun insertar(notificacion: NotificacionEntity)

    @Query("SELECT * FROM notificaciones WHERE usuarioId = :usuarioId ORDER BY id DESC")
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<NotificacionEntity>>

    @Query("SELECT COUNT(*) FROM notificaciones WHERE usuarioId = :usuarioId AND leida = 0")
    fun contarNoLeidas(usuarioId: Int): Flow<Int>

    @Query("UPDATE notificaciones SET leida = 1 WHERE usuarioId = :usuarioId")
    suspend fun marcarTodasLeidas(usuarioId: Int)
}