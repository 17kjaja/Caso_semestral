package com.example.reservasespacios.data.local

import androidx.room.Embedded

data class ReservaConDetalle(
    @Embedded val reserva: ReservaEntity,
    val espacioNombre: String,
    val solicitanteNombre: String
)