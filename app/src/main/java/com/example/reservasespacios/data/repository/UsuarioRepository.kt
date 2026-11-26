package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.UsuarioDao
import com.example.reservasespacios.data.local.UsuarioEntity
import com.example.reservasespacios.data.remote.RetrofitClient
import com.example.reservasespacios.data.remote.api.UsuarioApi
import com.example.reservasespacios.data.remote.dto.LoginRequest

class UsuarioRepository(
    private val dao: UsuarioDao,
    private val api: UsuarioApi = RetrofitClient.usuarioApi
) {
    suspend fun iniciarSesion(usuario: String, clave: String): UsuarioEntity? {
        return try {
            val dto = api.login(LoginRequest(usuario.trim(), clave))
            val entity = UsuarioEntity(
                id = dto.id ?: 0,
                nombre = dto.nombre,
                usuario = dto.usuario,
                clave = clave,
                perfil = dto.perfil
            )
            try {
                dao.insertar(entity)
            } catch (_: Exception) { }
            entity
        } catch (_: Exception) {
            dao.iniciarSesion(usuario, clave)
        }
    }
}