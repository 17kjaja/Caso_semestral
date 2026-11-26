package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.EspacioDao
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.data.remote.RetrofitClient
import com.example.reservasespacios.data.remote.api.EspacioApi
import com.example.reservasespacios.data.remote.dto.EspacioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class EspacioRepository(
    private val dao: EspacioDao,
    private val api: EspacioApi = RetrofitClient.espacioApi
) {
    val espacios: Flow<List<EspacioEntity>> = dao.obtenerTodos().onStart {
        sincronizar()
    }

    suspend fun sincronizar() {
        try {
            val remotos = api.listar()
            remotos.forEach { dto ->
                val entidad = dto.toEntity()
                if (dao.obtenerPorId(entidad.id) == null) {
                    dao.insertar(entidad)
                } else {
                    dao.actualizar(entidad)
                }
            }
        } catch (_: Exception) { }
    }

    suspend fun obtenerPorId(id: Int): EspacioEntity? {
        return try {
            val dto = api.obtener(id)
            val entidad = dto.toEntity()
            if (dao.obtenerPorId(entidad.id) == null) {
                dao.insertar(entidad)
            } else {
                dao.actualizar(entidad)
            }
            entidad
        } catch (_: Exception) {
            dao.obtenerPorId(id)
        }
    }

    suspend fun insertar(espacio: EspacioEntity) {
        try {
            val creado = api.crear(espacio.toDto(incluirId = false))
            dao.insertar(creado.toEntity())
        } catch (_: Exception) {
            dao.insertar(espacio)
        }
    }

    suspend fun actualizar(espacio: EspacioEntity) {
        try {
            val actualizado = api.actualizar(espacio.id, espacio.toDto(incluirId = true))
            dao.actualizar(actualizado.toEntity())
        } catch (_: Exception) {
            dao.actualizar(espacio)
        }
    }

    private fun EspacioDto.toEntity() = EspacioEntity(
        id = id ?: 0,
        nombre = nombre,
        tipo = tipo,
        descripcion = descripcion,
        ubicacion = ubicacion,
        capacidadMax = capacidadMax,
        horaApertura = horaApertura,
        horaCierre = horaCierre,
        estado = estado
    )

    private fun EspacioEntity.toDto(incluirId: Boolean) = EspacioDto(
        id = if (incluirId && id != 0) id else null,
        nombre = nombre,
        tipo = tipo,
        descripcion = descripcion,
        ubicacion = ubicacion,
        capacidadMax = capacidadMax,
        horaApertura = horaApertura,
        horaCierre = horaCierre,
        estado = estado
    )
}