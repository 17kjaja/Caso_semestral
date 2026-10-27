package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BloqueoDao {

    @Insert
    suspend fun insertar(bloqueo: BloqueoEntity)

    @Query(
        """
        SELECT COUNT(*) FROM bloqueos
        WHERE espacioId = :espacioId AND fecha = :fecha
        AND :inicio < horaFin AND :fin > horaInicio
        """
    )
    suspend fun contarTraslapes(espacioId: Int, fecha: String, inicio: String, fin: String): Int

    @Query(
        """
        SELECT b.*, e.nombre AS espacioNombre
        FROM bloqueos b
        INNER JOIN espacios e ON e.id = b.espacioId
        ORDER BY b.fecha, b.horaInicio
        """
    )
    fun obtenerConDetalle(): Flow<List<BloqueoConDetalle>>

    @Query("DELETE FROM bloqueos WHERE id = :id")
    suspend fun eliminar(id: Int)
}