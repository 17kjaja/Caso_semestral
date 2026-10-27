package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EspacioDao {

    @Query("SELECT * FROM espacios ORDER BY nombre")
    fun obtenerTodos(): Flow<List<EspacioEntity>>

    @Query("SELECT COUNT(*) FROM espacios")
    suspend fun contar(): Int

    @Query("SELECT * FROM espacios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): EspacioEntity?

    @Insert
    suspend fun insertar(espacio: EspacioEntity)

    @Update
    suspend fun actualizar(espacio: EspacioEntity)
}