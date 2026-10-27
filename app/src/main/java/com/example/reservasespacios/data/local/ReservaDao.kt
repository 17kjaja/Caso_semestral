package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Insert
    suspend fun insertar(reserva: ReservaEntity)

    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas WHERE estado = :estado ORDER BY fecha")
    fun obtenerPorEstado(estado: String): Flow<List<ReservaEntity>>

    @Query("""
        SELECT COUNT(*) FROM reservas
        WHERE espacioId = :espacioId AND fecha = :fecha
        AND estado IN ('PENDIENTE', 'ACEPTADA')
        AND :inicio < horaFin AND :fin > horaInicio
    """)
    suspend fun contarTraslapes(espacioId: Int, fecha: String, inicio: String, fin: String): Int

    @Query("""
        UPDATE reservas
        SET estado = :estado, justificacion = :justificacion,
            adminId = :adminId, fechaResolucion = :fechaResolucion
        WHERE id = :id
    """)
    suspend fun resolver(id: Int, estado: String, justificacion: String, adminId: Int, fechaResolucion: String)
}