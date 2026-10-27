package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.BloqueoConDetalle
import com.example.reservasespacios.data.local.BloqueoEntity
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.data.repository.BloqueoRepository
import com.example.reservasespacios.data.repository.EspacioRepository
import com.example.reservasespacios.domain.validation.BloqueoValidator
import com.example.reservasespacios.domain.validation.ReservaValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BloqueosUiState(
    val espacioId: Int = 0,
    val fecha: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val motivo: String = "",
    val espacioError: String? = null,
    val fechaError: String? = null,
    val horaInicioError: String? = null,
    val horaFinError: String? = null,
    val motivoError: String? = null,
    val errorGeneral: String? = null,
    val mensajeExito: String? = null
)

class BloqueosViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.obtener(application)
    private val espacioRepository = EspacioRepository(db.espacioDao())
    private val bloqueoRepository = BloqueoRepository(db.bloqueoDao())

    val espacios: StateFlow<List<EspacioEntity>> = espacioRepository.espacios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bloqueos: StateFlow<List<BloqueoConDetalle>> = bloqueoRepository.bloqueos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(BloqueosUiState())
    val uiState: StateFlow<BloqueosUiState> = _uiState.asStateFlow()

    fun onEspacioChange(id: Int) =
        _uiState.update { it.copy(espacioId = id, espacioError = null, errorGeneral = null) }

    fun onFechaChange(v: String) =
        _uiState.update { it.copy(fecha = v, fechaError = null, errorGeneral = null) }

    fun onHoraInicioChange(v: String) =
        _uiState.update { it.copy(horaInicio = v, horaInicioError = null, errorGeneral = null) }

    fun onHoraFinChange(v: String) =
        _uiState.update { it.copy(horaFin = v, horaFinError = null, errorGeneral = null) }

    fun onMotivoChange(v: String) =
        _uiState.update { it.copy(motivo = v, motivoError = null, errorGeneral = null) }

    fun consumirMensaje() = _uiState.update { it.copy(mensajeExito = null) }

    fun eliminar(id: Int) {
        viewModelScope.launch { bloqueoRepository.eliminar(id) }
    }

    fun guardar() {
        val e = _uiState.value
        val espacio = espacios.value.firstOrNull { it.id == e.espacioId }
        val apertura = espacio?.horaApertura ?: ""
        val cierre = espacio?.horaCierre ?: ""

        val rEspacio = BloqueoValidator.validarEspacio(e.espacioId)
        val rFecha = BloqueoValidator.validarFecha(e.fecha)
        val rInicio = ReservaValidator.validarHoraInicio(e.horaInicio, apertura, cierre)
        val rFin = ReservaValidator.validarHoraFin(e.horaInicio, e.horaFin, cierre)
        val rMotivo = BloqueoValidator.validarMotivo(e.motivo)

        _uiState.update {
            it.copy(
                espacioError = rEspacio.error,
                fechaError = rFecha.error,
                horaInicioError = rInicio.error,
                horaFinError = rFin.error,
                motivoError = rMotivo.error
            )
        }

        val todoOk = listOf(rEspacio, rFecha, rInicio, rFin, rMotivo).all { it.ok }
        if (!todoOk) return

        val fecha = e.fecha.trim()
        val inicio = e.horaInicio.trim()
        val fin = e.horaFin.trim()

        viewModelScope.launch {
            if (bloqueoRepository.hayBloqueo(e.espacioId, fecha, inicio, fin)) {
                _uiState.update { it.copy(errorGeneral = "Ya existe un bloqueo en ese horario") }
            } else {
                bloqueoRepository.crear(
                    BloqueoEntity(
                        espacioId = e.espacioId,
                        fecha = fecha,
                        horaInicio = inicio,
                        horaFin = fin,
                        motivo = e.motivo.trim()
                    )
                )
                _uiState.value = BloqueosUiState(mensajeExito = "Bloqueo registrado")
            }
        }
    }
}