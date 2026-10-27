package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.data.repository.ReservaRepository
import com.example.reservasespacios.util.SesionActual
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MisSolicitudesUiState(
    val reservaACancelar: ReservaConDetalle? = null
)

class MisSolicitudesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReservaRepository(AppDatabase.obtener(application).reservaDao())

    val reservas: StateFlow<List<ReservaConDetalle>> = repository
        .misReservas(SesionActual.usuarioId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(MisSolicitudesUiState())
    val uiState: StateFlow<MisSolicitudesUiState> = _uiState.asStateFlow()

    fun pedirCancelar(reserva: ReservaConDetalle) {
        _uiState.update { it.copy(reservaACancelar = reserva) }
    }

    fun descartarCancelar() {
        _uiState.update { it.copy(reservaACancelar = null) }
    }

    fun confirmarCancelar() {
        val reserva = _uiState.value.reservaACancelar ?: return
        viewModelScope.launch {
            repository.cancelar(reserva.reserva.id)
            _uiState.update { it.copy(reservaACancelar = null) }
        }
    }
}