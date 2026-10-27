package com.example.reservasespacios.domain.validation

data class ValidationResult(val ok: Boolean, val error: String? = null)

object LoginValidator {

    fun validarUsuario(usuario: String): ValidationResult =
        if (usuario.isBlank()) ValidationResult(false, "Ingresa tu usuario")
        else ValidationResult(true)

    fun validarClave(clave: String): ValidationResult =
        when {
            clave.isBlank() -> ValidationResult(false, "Ingresa tu clave")
            clave.length < 4 -> ValidationResult(false, "La clave debe tener al menos 4 caracteres")
            else -> ValidationResult(true)
        }
}