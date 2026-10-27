package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.data.local.ReservaDao
import com.example.reservasespacios.data.local.ReservaEntity
import kotlinx.coroutines.flow.Flow

class ReservaRepository(private val dao: ReservaDao) {

    val pendientes: Flow<List<ReservaConDetalle>> = dao.obtenerPendientesConDetalle()

    fun misReservas(usuarioId: Int): Flow<List<ReservaConDetalle>> =
        dao.obtenerConDetallePorUsuario(usuarioId)

    suspend fun hayTraslape(espacioId: Int, fecha: String, inicio: String, fin: String): Boolean =
        dao.contarTraslapes(espacioId, fecha, inicio, fin) > 0

    suspend fun crear(reserva: ReservaEntity) = dao.insertar(reserva)

    suspend fun cancelar(id: Int) = dao.cancelar(id)

    suspend fun resolver(id: Int, estado: String, justificacion: String, adminId: Int, fecha: String) =
        dao.resolver(id, estado, justificacion, adminId, fecha)
}