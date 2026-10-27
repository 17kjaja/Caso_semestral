package com.example.reservasespacios.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "espacios")
data class EspacioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val ubicacion: String,
    val capacidadMax: Int,
    val horaApertura: String,
    val horaCierre: String,
    val estado: String
)