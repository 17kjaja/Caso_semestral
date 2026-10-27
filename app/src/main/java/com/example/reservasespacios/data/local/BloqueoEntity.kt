package com.example.reservasespacios.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bloqueos")
data class BloqueoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val espacioId: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val motivo: String
)

data class BloqueoConDetalle(
    @Embedded val bloqueo: BloqueoEntity,
    val espacioNombre: String
)