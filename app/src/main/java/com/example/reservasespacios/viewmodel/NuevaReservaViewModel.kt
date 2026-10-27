package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.data.local.ReservaEntity
import com.example.reservasespacios.data.repository.BloqueoRepository
import com.example.reservasespacios.data.repository.EspacioRepository
import com.example.reservasespacios.data.repository.ReservaRepository
import com.example.reservasespacios.domain.validation.ReservaValidator
import com.example.reservasespacios.util.SesionActual
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class NuevaReservaUiState(
    val espacio: EspacioEntity? = null,
    val actividad: String = "",
    val cursoGrupo: String = "",
    val asistentes: String = "",
    val fecha: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val observaciones: String = "",
    val actividadError: String? = null,
    val cursoError: String? = null,
    val asistentesError: String? = null,
    val fechaError: String? = null,
    val horaInicioError: String? = null,
    val horaFinError: String? = null,
    val errorGeneral: String? = null,
    val mostrarConfirmacion: Boolean = false,
    val cargando: Boolean = false,
    val guardadoExitoso: Boolean = false
)

class NuevaReservaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.obtener(application)
    private val espacioRepository = EspacioRepository(db.espacioDao())
    private val reservaRepository = ReservaRepository(db.reservaDao())
    private val bloqueoRepository = BloqueoRepository(db.bloqueoDao())

    private val _uiState = MutableStateFlow(NuevaReservaUiState())
    val uiState: StateFlow<NuevaReservaUiState> = _uiState.asStateFlow()

    fun cargarEspacio(id: Int) {
        viewModelScope.launch {
            val espacio = espacioRepository.obtenerPorId(id)
            _uiState.update { it.copy(espacio = espacio) }
        }
    }

    fun onActividadChange(valor: String) =
        _uiState.update { it.copy(actividad = valor, actividadError = null, errorGeneral = null) }

    fun onCursoChange(valor: String) =
        _uiState.update { it.copy(cursoGrupo = valor, cursoError = null, errorGeneral = null) }

    fun onAsistentesChange(valor: String) =
        _uiState.update { it.copy(asistentes = valor, asistentesError = null, errorGeneral = null) }

    fun onFechaChange(valor: String) =
        _uiState.update { it.copy(fecha = valor, fechaError = null, errorGeneral = null) }

    fun onHoraInicioChange(valor: String) =
        _uiState.update { it.copy(horaInicio = valor, horaInicioError = null, errorGeneral = null) }

    fun onHoraFinChange(valor: String) =
        _uiState.update { it.copy(horaFin = valor, horaFinError = null, errorGeneral = null) }

    fun onObservacionesChange(valor: String) =
        _uiState.update { it.copy(observaciones = valor) }

    fun onEnviarClick() {
        val e = _uiState.value
        val espacio = e.espacio ?: return

        val rActividad = ReservaValidator.validarActividad(e.actividad)
        val rCurso = ReservaValidator.validarCurso(e.cursoGrupo)
        val rAsistentes = ReservaValidator.validarAsistentes(e.asistentes, espacio.capacidadMax)
        val rFecha = ReservaValidator.validarFecha(e.fecha)
        val rInicio = ReservaValidator.validarHoraInicio(e.horaInicio, espacio.horaApertura, espacio.horaCierre)
        val rFin = ReservaValidator.validarHoraFin(e.horaInicio, e.horaFin, espacio.horaCierre)

        val todoOk = listOf(rActividad, rCurso, rAsistentes, rFecha, rInicio, rFin).all { it.ok }

        _uiState.update {
            it.copy(
                actividadError = rActividad.error,
                cursoError = rCurso.error,
                asistentesError = rAsistentes.error,
                fechaError = rFecha.error,
                horaInicioError = rInicio.error,
                horaFinError = rFin.error,
                mostrarConfirmacion = todoOk
            )
        }
    }

    fun cancelarConfirmacion() {
        _uiState.update { it.copy(mostrarConfirmacion = false) }
    }

    fun confirmarEnvio() {
        val e = _uiState.value
        val espacio = e.espacio ?: return
        val inicio = ReservaValidator.parseHora(e.horaInicio)?.toString() ?: return
        val fin = ReservaValidator.parseHora(e.horaFin)?.toString() ?: return
        val fecha = e.fecha.trim()

        viewModelScope.launch {
            _uiState.update { it.copy(mostrarConfirmacion = false, cargando = true) }

            val mensajeError = when {
                espacio.estado != "DISPONIBLE" ->
                    "El espacio no está disponible para reservas"
                bloqueoRepository.hayBloqueo(espacio.id, fecha, inicio, fin) ->
                    "El espacio está bloqueado en ese horario"
                reservaRepository.hayTraslape(espacio.id, fecha, inicio, fin) ->
                    "Ya existe una reserva para ese espacio en ese horario"
                else -> null
            }

            if (mensajeError != null) {
                _uiState.update { it.copy(cargando = false, errorGeneral = mensajeError) }
            } else {
                reservaRepository.crear(
                    ReservaEntity(
                        espacioId = espacio.id,
                        usuarioId = SesionActual.usuarioId,
                        fecha = fecha,
                        horaInicio = inicio,
                        horaFin = fin,
                        actividad = e.actividad.trim(),
                        cursoGrupo = e.cursoGrupo.trim(),
                        asistentes = e.asistentes.trim().toInt(),
                        observaciones = e.observaciones.trim(),
                        fechaCreacion = LocalDate.now().toString()
                    )
                )
                _uiState.update { it.copy(cargando = false, guardadoExitoso = true) }
            }
        }
    }
}