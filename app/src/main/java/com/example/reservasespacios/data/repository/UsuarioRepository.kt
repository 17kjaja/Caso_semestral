package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.UsuarioDao
import com.example.reservasespacios.data.local.UsuarioEntity

class UsuarioRepository(private val dao: UsuarioDao) {

    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity? =
        dao.iniciarSesion(usuario, clave)
}