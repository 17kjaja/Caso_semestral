package com.example.reservasespacios.data.remote.dto

// ---------- usuarios-service ----------
data class LoginRequest(val usuario: String, val clave: String)

data class UsuarioDto(
    val id: Int? = null,
    val nombre: String,
    val usuario: String,
    val clave: String? = null,   // solo se envía al crear/editar; el servidor nunca la devuelve
    val perfil: String
)

// ---------- espacios-service ----------
data class EspacioDto(
    val id: Int? = null,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val ubicacion: String,
    val capacidadMax: Int,
    val horaApertura: String,
    val horaCierre: String,
    val estado: String
)

data class BloqueoRequest(
    val espacioId: Int,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val motivo: String
)

data class BloqueoDto(
    val id: Int,
    val espacioId: Int,
    val espacioNombre: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val motivo: String
)

// ---------- reservas-service ----------
data class ReservaDto(
    val id: Int? = null,
    val espacioId: Int,
    val usuarioId: Int,
    val espacioNombre: String,
    val solicitanteNombre: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val actividad: String,
    val cursoGrupo: String,
    val asistentes: Int,
    val observaciones: String = "",
    val fechaCreacion: String? = null,
    val estado: String? = null,
    val justificacion: String? = null,
    val adminId: Int? = null,
    val fechaResolucion: String? = null
)

data class ResolverRequest(
    val estado: String,          // "ACEPTADA" | "RECHAZADA"
    val justificacion: String,
    val adminId: Int
)

data class NotificacionDto(
    val id: Int? = null,
    val usuarioId: Int,
    val titulo: String,
    val mensaje: String,
    val leida: Boolean = false,
    val fecha: String? = null
)

// ---------- API externa (Nager.Date) ----------
data class FeriadoDto(
    val date: String,        // "2026-12-25"
    val localName: String,   // "Navidad"
    val name: String         // "Christmas Day"
)
