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

    @Query(
        """
        SELECT COUNT(*) FROM reservas
        WHERE espacioId = :espacioId AND fecha = :fecha
        AND estado IN ('PENDIENTE', 'ACEPTADA')
        AND :inicio < horaFin AND :fin > horaInicio
        """
    )
    suspend fun contarTraslapes(espacioId: Int, fecha: String, inicio: String, fin: String): Int

    @Query(
        """
        UPDATE reservas
        SET estado = :estado, justificacion = :justificacion,
            adminId = :adminId, fechaResolucion = :fechaResolucion
        WHERE id = :id
        """
    )
    suspend fun resolver(
        id: Int,
        estado: String,
        justificacion: String,
        adminId: Int,
        fechaResolucion: String
    )

    @Query(
        """
        SELECT r.*, e.nombre AS espacioNombre, u.nombre AS solicitanteNombre
        FROM reservas r
        INNER JOIN espacios e ON e.id = r.espacioId
        INNER JOIN usuarios u ON u.id = r.usuarioId
        WHERE r.usuarioId = :usuarioId
        ORDER BY r.fecha DESC, r.horaInicio DESC
        """
    )
    fun obtenerConDetallePorUsuario(usuarioId: Int): Flow<List<ReservaConDetalle>>

    @Query(
        """
        SELECT r.*, e.nombre AS espacioNombre, u.nombre AS solicitanteNombre
        FROM reservas r
        INNER JOIN espacios e ON e.id = r.espacioId
        INNER JOIN usuarios u ON u.id = r.usuarioId
        WHERE r.estado = 'PENDIENTE'
        ORDER BY r.fecha, r.horaInicio
        """
    )
    fun obtenerPendientesConDetalle(): Flow<List<ReservaConDetalle>>

    @Query("UPDATE reservas SET estado = 'CANCELADA' WHERE id = :id")
    suspend fun cancelar(id: Int)
}