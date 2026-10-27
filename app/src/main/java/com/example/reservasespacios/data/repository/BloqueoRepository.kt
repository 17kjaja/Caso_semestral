package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.BloqueoConDetalle
import com.example.reservasespacios.data.local.BloqueoDao
import com.example.reservasespacios.data.local.BloqueoEntity
import kotlinx.coroutines.flow.Flow

class BloqueoRepository(private val dao: BloqueoDao) {

    val bloqueos: Flow<List<BloqueoConDetalle>> = dao.obtenerConDetalle()

    suspend fun hayBloqueo(espacioId: Int, fecha: String, inicio: String, fin: String): Boolean =
        dao.contarTraslapes(espacioId, fecha, inicio, fin) > 0

    suspend fun crear(bloqueo: BloqueoEntity) = dao.insertar(bloqueo)

    suspend fun eliminar(id: Int) = dao.eliminar(id)
}