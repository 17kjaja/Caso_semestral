package com.example.reservasespacios.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val espacioId: Int,
    val usuarioId: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val actividad: String,
    val cursoGrupo: String,
    val asistentes: Int,
    val observaciones: String = "",
    val fechaCreacion: String,
    val estado: String = "PENDIENTE",
    val justificacion: String = "",
    val adminId: Int? = null,
    val fechaResolucion: String? = null
)