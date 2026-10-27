package com.example.reservasespacios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND clave = :clave LIMIT 1")
    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity?

    @Insert
    suspend fun insertar(usuario: UsuarioEntity)
}