package com.example.reservasespacios.data.repository

import com.example.reservasespacios.data.local.EspacioDao
import com.example.reservasespacios.data.local.EspacioEntity
import kotlinx.coroutines.flow.Flow

class EspacioRepository(private val dao: EspacioDao) {

    val espacios: Flow<List<EspacioEntity>> = dao.obtenerTodos()
}