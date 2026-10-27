package com.example.reservasespacios.domain.validation

import java.time.LocalDate
import java.time.format.DateTimeParseException

object BloqueoValidator {

    fun validarEspacio(espacioId: Int): ValidationResult =
        if (espacioId == 0) ValidationResult(false, "Selecciona un espacio")
        else ValidationResult(true)

    fun validarMotivo(texto: String): ValidationResult = when {
        texto.isBlank() -> ValidationResult(false, "Indica el motivo del bloqueo")
        texto.trim().length < 5 -> ValidationResult(false, "El motivo debe tener al menos 5 caracteres")
        else -> ValidationResult(true)
    }

    fun validarFecha(texto: String, hoy: LocalDate = LocalDate.now()): ValidationResult {
        if (texto.isBlank()) return ValidationResult(false, "Indica la fecha")
        val fecha = try {
            LocalDate.parse(texto.trim())
        } catch (e: DateTimeParseException) {
            return ValidationResult(false, "Usa el formato AAAA-MM-DD, por ejemplo 2026-11-05")
        }
        if (fecha.isBefore(hoy)) {
            return ValidationResult(false, "No se puede bloquear una fecha pasada")
        }
        return ValidationResult(true)
    }
}