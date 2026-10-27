package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.data.repository.ReservaRepository
import com.example.reservasespacios.domain.validation.ReservaValidator
import com.example.reservasespacios.util.SesionActual
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AdminUiState(
    val reservaAAprobar: ReservaConDetalle? = null,
    val reservaARechazar: ReservaConDetalle? = null,
    val justificacion: String = "",
    val justificacionError: String? = null
)

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReservaRepository(AppDatabase.obtener(application).reservaDao())

    val pendientes: StateFlow<List<ReservaConDetalle>> = repository.pendientes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    fun pedirAprobar(reserva: ReservaConDetalle) {
        _uiState.update { it.copy(reservaAAprobar = reserva) }
    }

    fun pedirRechazar(reserva: ReservaConDetalle) {
        _uiState.update { it.copy(reservaARechazar = reserva, justificacion = "", justificacionError = null) }
    }

    fun descartar() {
        _uiState.update { AdminUiState() }
    }

    fun onJustificacionChange(valor: String) {
        _uiState.update { it.copy(justificacion = valor, justificacionError = null) }
    }

    fun confirmarAprobar() {
        val reserva = _uiState.value.reservaAAprobar ?: return
        viewModelScope.launch {
            repository.resolver(
                id = reserva.reserva.id,
                estado = "ACEPTADA",
                justificacion = "",
                adminId = SesionActual.usuarioId,
                fecha = LocalDate.now().toString()
            )
            _uiState.update { AdminUiState() }
        }
    }

    fun confirmarRechazar() {
        val estado = _uiState.value
        val reserva = estado.reservaARechazar ?: return

        val resultado = ReservaValidator.validarJustificacion(estado.justificacion)
        if (!resultado.ok) {
            _uiState.update { it.copy(justificacionError = resultado.error) }
            return
        }

        viewModelScope.launch {
            repository.resolver(
                id = reserva.reserva.id,
                estado = "RECHAZADA",
                justificacion = estado.justificacion.trim(),
                adminId = SesionActual.usuarioId,
                fecha = LocalDate.now().toString()
            )
            _uiState.update { AdminUiState() }
        }
    }
}