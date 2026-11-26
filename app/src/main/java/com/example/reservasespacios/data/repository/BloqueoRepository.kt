package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.BloqueoConDetalle
import com.example.reservasespacios.data.local.BloqueoDao
import com.example.reservasespacios.data.local.BloqueoEntity
import com.example.reservasespacios.data.remote.RetrofitClient
import com.example.reservasespacios.data.remote.api.BloqueoApi
import com.example.reservasespacios.data.remote.dto.BloqueoRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class BloqueoRepository(
    private val dao: BloqueoDao,
    private val api: BloqueoApi = RetrofitClient.bloqueoApi
) {
    val bloqueos: Flow<List<BloqueoConDetalle>> = dao.obtenerConDetalle().onStart {
        sincronizar()
    }

    suspend fun sincronizar() {
        try {
            api.listar().forEach { dto ->
                dao.insertar(
                    BloqueoEntity(
                        id = dto.id,
                        espacioId = dto.espacioId,
                        fecha = dto.fecha,
                        horaInicio = dto.horaInicio,
                        horaFin = dto.horaFin,
                        motivo = dto.motivo
                    )
                )
            }
        } catch (_: Exception) { }
    }

    suspend fun hayBloqueo(espacioId: Int, fecha: String, inicio: String, fin: String): Boolean {
        return try {
            api.hayTraslape(espacioId, fecha, inicio, fin)
        } catch (_: Exception) {
            dao.contarTraslapes(espacioId, fecha, inicio, fin) > 0
        }
    }

    suspend fun crear(bloqueo: BloqueoEntity) {
        try {
            val creado = api.crear(
                BloqueoRequest(
                    espacioId = bloqueo.espacioId,
                    fecha = bloqueo.fecha,
                    horaInicio = bloqueo.horaInicio,
                    horaFin = bloqueo.horaFin,
                    motivo = bloqueo.motivo
                )
            )
            dao.insertar(bloqueo.copy(id = creado.id))
        } catch (_: Exception) {
            dao.insertar(bloqueo)
        }
    }

    suspend fun eliminar(id: Int) {
        try {
            api.eliminar(id)
        } catch (_: Exception) { }
        dao.eliminar(id)
    }
}