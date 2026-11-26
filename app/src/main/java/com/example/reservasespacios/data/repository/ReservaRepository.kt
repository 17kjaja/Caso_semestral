package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.data.local.ReservaDao
import com.example.reservasespacios.data.local.ReservaEntity
import com.example.reservasespacios.data.remote.RetrofitClient
import com.example.reservasespacios.data.remote.api.ReservaApi
import com.example.reservasespacios.data.remote.dto.ReservaDto
import com.example.reservasespacios.data.remote.dto.ResolverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ReservaRepository(
    private val dao: ReservaDao,
    private val api: ReservaApi = RetrofitClient.reservaApi
) {
    val pendientes: Flow<List<ReservaConDetalle>> = dao.obtenerPendientesConDetalle().onStart {
        sincronizar()
    }

    fun misReservas(usuarioId: Int): Flow<List<ReservaConDetalle>> =
        dao.obtenerConDetallePorUsuario(usuarioId).onStart {
            sincronizarPorUsuario(usuarioId)
        }

    suspend fun sincronizar() {
        try {
            api.listar().forEach { dao.insertar(it.toEntity()) }
        } catch (_: Exception) { }
    }

    suspend fun sincronizarPorUsuario(usuarioId: Int) {
        try {
            api.porUsuario(usuarioId).forEach { dao.insertar(it.toEntity()) }
        } catch (_: Exception) { }
    }

    suspend fun hayTraslape(espacioId: Int, fecha: String, inicio: String, fin: String): Boolean {
        return try {
            api.hayTraslape(espacioId, fecha, inicio, fin)
        } catch (_: Exception) {
            dao.contarTraslapes(espacioId, fecha, inicio, fin) > 0
        }
    }

    suspend fun crear(reserva: ReservaEntity, espacioNombre: String = "", solicitanteNombre: String = "") {
        try {
            val dto = ReservaDto(
                espacioId = reserva.espacioId,
                usuarioId = reserva.usuarioId,
                espacioNombre = espacioNombre,
                solicitanteNombre = solicitanteNombre,
                fecha = reserva.fecha,
                horaInicio = reserva.horaInicio,
                horaFin = reserva.horaFin,
                actividad = reserva.actividad,
                cursoGrupo = reserva.cursoGrupo,
                asistentes = reserva.asistentes,
                observaciones = reserva.observaciones,
                fechaCreacion = reserva.fechaCreacion,
                estado = reserva.estado
            )
            val creada = api.crear(dto)
            dao.insertar(creada.toEntity())
        } catch (_: Exception) {
            dao.insertar(reserva)
        }
    }

    suspend fun cancelar(id: Int) {
        try {
            api.cancelar(id)
        } catch (_: Exception) { }
        dao.cancelar(id)
    }

    suspend fun resolver(id: Int, estado: String, justificacion: String, adminId: Int, fecha: String) {
        try {
            api.resolver(id, ResolverRequest(estado = estado, justificacion = justificacion, adminId = adminId))
        } catch (_: Exception) { }
        dao.resolver(id, estado, justificacion, adminId, fecha)
    }

    private fun ReservaDto.toEntity() = ReservaEntity(
        id = id ?: 0,
        espacioId = espacioId,
        usuarioId = usuarioId,
        fecha = fecha,
        horaInicio = horaInicio,
        horaFin = horaFin,
        actividad = actividad,
        cursoGrupo = cursoGrupo,
        asistentes = asistentes,
        observaciones = observaciones,
        fechaCreacion = fechaCreacion ?: "",
        estado = estado ?: "PENDIENTE",
        justificacion = justificacion ?: "",
        adminId = adminId,
        fechaResolucion = fechaResolucion
    )
}