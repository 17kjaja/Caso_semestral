package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EspacioDao {

    @Query("SELECT * FROM espacios ORDER BY nombre")
    fun obtenerTodos(): Flow<List<EspacioEntity>>

    @Query("SELECT COUNT(*) FROM espacios")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(espacio: EspacioEntity)
}