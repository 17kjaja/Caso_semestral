package com.example.reservasespacios.domain.validation

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException

object ReservaValidator {

    fun proximoDiaHabil(desde: LocalDate): LocalDate {
        var dia = desde.plusDays(1)
        while (dia.dayOfWeek == DayOfWeek.SATURDAY || dia.dayOfWeek == DayOfWeek.SUNDAY) {
            dia = dia.plusDays(1)
        }
        return dia
    }

    fun validarActividad(texto: String): ValidationResult = when {
        texto.isBlank() -> ValidationResult(false, "Indica la actividad")
        texto.trim().length < 3 -> ValidationResult(false, "Describe la actividad con más detalle")
        else -> ValidationResult(true)
    }

    fun validarCurso(texto: String): ValidationResult =
        if (texto.isBlank()) ValidationResult(false, "Indica el curso o grupo")
        else ValidationResult(true)

    fun validarAsistentes(texto: String, capacidad: Int): ValidationResult {
        val cantidad = texto.trim().toIntOrNull()
        return when {
            texto.isBlank() -> ValidationResult(false, "Indica la cantidad de asistentes")
            cantidad == null -> ValidationResult(false, "Ingresa solo números")
            cantidad <= 0 -> ValidationResult(false, "Debe haber al menos 1 asistente")
            cantidad > capacidad -> ValidationResult(false, "Supera la capacidad máxima ($capacidad)")
            else -> ValidationResult(true)
        }
    }

    fun validarFecha(texto: String, hoy: LocalDate = LocalDate.now()): ValidationResult {
        if (texto.isBlank()) return ValidationResult(false, "Indica la fecha")
        val fecha = try {
            LocalDate.parse(texto.trim())
        } catch (e: DateTimeParseException) {
            return ValidationResult(false, "Usa el formato AAAA-MM-DD, por ejemplo 2026-11-05")
        }
        if (fecha.dayOfWeek == DayOfWeek.SATURDAY || fecha.dayOfWeek == DayOfWeek.SUNDAY) {
            return ValidationResult(false, "Solo se puede reservar de lunes a viernes")
        }
        if (fecha.isBefore(proximoDiaHabil(hoy))) {
            return ValidationResult(false, "Debe solicitarse con al menos 1 día hábil de anticipación")
        }
        return ValidationResult(true)
    }

    fun parseHora(texto: String): LocalTime? {
        val limpio = texto.trim()
        if (!Regex("""^\d{2}:\d{2}$""").matches(limpio)) return null
        return try {
            LocalTime.parse(limpio)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun validarHoraInicio(inicio: String, apertura: String, cierre: String): ValidationResult {
        if (inicio.isBlank()) return ValidationResult(false, "Indica la hora de inicio")
        val hora = parseHora(inicio)
            ?: return ValidationResult(false, "Usa el formato HH:mm, por ejemplo 09:30")
        val abre = parseHora(apertura)
        val cierra = parseHora(cierre)
        if (abre != null && hora.isBefore(abre)) {
            return ValidationResult(false, "El espacio abre a las $apertura")
        }
        if (cierra != null && !hora.isBefore(cierra)) {
            return ValidationResult(false, "El espacio cierra a las $cierre")
        }
        return ValidationResult(true)
    }

    fun validarHoraFin(inicio: String, fin: String, cierre: String): ValidationResult {
        if (fin.isBlank()) return ValidationResult(false, "Indica la hora de término")
        val hora = parseHora(fin)
            ?: return ValidationResult(false, "Usa el formato HH:mm, por ejemplo 10:30")
        val desde = parseHora(inicio)
        val cierra = parseHora(cierre)
        if (desde != null && !hora.isAfter(desde)) {
            return ValidationResult(false, "La hora de término debe ser posterior al inicio")
        }
        if (cierra != null && hora.isAfter(cierra)) {
            return ValidationResult(false, "El espacio cierra a las $cierre")
        }
        return ValidationResult(true)
    }
    fun validarJustificacion(texto: String): ValidationResult = when {
        texto.isBlank() -> ValidationResult(false, "Escribe la justificación del rechazo")
        texto.trim().length < 5 -> ValidationResult(false, "La justificación debe tener al menos 5 caracteres")
        else -> ValidationResult(true)
    }
}