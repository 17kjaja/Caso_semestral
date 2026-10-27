package com.example.reservasespacios.domain.validation

object EspacioValidator {

    fun validarNombre(texto: String): ValidationResult = when {
        texto.isBlank() -> ValidationResult(false, "Indica el nombre del espacio")
        texto.trim().length < 3 -> ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
        else -> ValidationResult(true)
    }

    fun validarTipo(texto: String): ValidationResult =
        if (texto.isBlank()) ValidationResult(false, "Indica el tipo de espacio")
        else ValidationResult(true)

    fun validarDescripcion(texto: String): ValidationResult =
        if (texto.isBlank()) ValidationResult(false, "Indica una descripción")
        else ValidationResult(true)

    fun validarUbicacion(texto: String): ValidationResult =
        if (texto.isBlank()) ValidationResult(false, "Indica la ubicación")
        else ValidationResult(true)

    fun validarCapacidad(texto: String): ValidationResult {
        val cantidad = texto.trim().toIntOrNull()
        return when {
            texto.isBlank() -> ValidationResult(false, "Indica la capacidad máxima")
            cantidad == null -> ValidationResult(false, "Ingresa solo números")
            cantidad <= 0 -> ValidationResult(false, "La capacidad debe ser mayor a 0")
            cantidad > 500 -> ValidationResult(false, "La capacidad máxima permitida es 500")
            else -> ValidationResult(true)
        }
    }

    fun validarApertura(texto: String): ValidationResult = when {
        texto.isBlank() -> ValidationResult(false, "Indica la hora de apertura")
        ReservaValidator.parseHora(texto) == null ->
            ValidationResult(false, "Usa el formato HH:mm, por ejemplo 08:00")
        else -> ValidationResult(true)
    }

    fun validarCierre(apertura: String, cierre: String): ValidationResult {
        if (cierre.isBlank()) return ValidationResult(false, "Indica la hora de cierre")
        val hora = ReservaValidator.parseHora(cierre)
            ?: return ValidationResult(false, "Usa el formato HH:mm, por ejemplo 17:00")
        val abre = ReservaValidator.parseHora(apertura)
        if (abre != null && !hora.isAfter(abre)) {
            return ValidationResult(false, "El cierre debe ser posterior a la apertura")
        }
        return ValidationResult(true)
    }
}