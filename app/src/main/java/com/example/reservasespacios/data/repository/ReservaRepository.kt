package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.ReservaDao
import com.example.reservasespacios.data.local.ReservaEntity

class ReservaRepository(private val dao: ReservaDao) {

    suspend fun hayTraslape(espacioId: Int, fecha: String, inicio: String, fin: String): Boolean =
        dao.contarTraslapes(espacioId, fecha, inicio, fin) > 0

    suspend fun crear(reserva: ReservaEntity) = dao.insertar(reserva)
}