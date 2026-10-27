package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.data.repository.EspacioRepository
import com.example.reservasespacios.domain.validation.EspacioValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EspacioFormUiState(
    val id: Int = 0,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val ubicacion: String = "",
    val capacidad: String = "",
    val horaApertura: String = "08:00",
    val horaCierre: String = "17:00",
    val estado: String = "DISPONIBLE",
    val nombreError: String? = null,
    val tipoError: String? = null,
    val descripcionError: String? = null,
    val ubicacionError: String? = null,
    val capacidadError: String? = null,
    val aperturaError: String? = null,
    val cierreError: String? = null,
    val guardadoExitoso: Boolean = false
)

class EspacioFormViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EspacioRepository(AppDatabase.obtener(application).espacioDao())

    private val _uiState = MutableStateFlow(EspacioFormUiState())
    val uiState: StateFlow<EspacioFormUiState> = _uiState.asStateFlow()

    fun cargar(id: Int) {
        if (id == 0) return
        viewModelScope.launch {
            repository.obtenerPorId(id)?.let { e ->
                _uiState.update {
                    it.copy(
                        id = e.id,
                        nombre = e.nombre,
                        tipo = e.tipo,
                        descripcion = e.descripcion,
                        ubicacion = e.ubicacion,
                        capacidad = e.capacidadMax.toString(),
                        horaApertura = e.horaApertura,
                        horaCierre = e.horaCierre,
                        estado = e.estado
                    )
                }
            }
        }
    }

    fun onNombreChange(v: String) = _uiState.update { it.copy(nombre = v, nombreError = null) }
    fun onTipoChange(v: String) = _uiState.update { it.copy(tipo = v, tipoError = null) }
    fun onDescripcionChange(v: String) = _uiState.update { it.copy(descripcion = v, descripcionError = null) }
    fun onUbicacionChange(v: String) = _uiState.update { it.copy(ubicacion = v, ubicacionError = null) }
    fun onCapacidadChange(v: String) = _uiState.update { it.copy(capacidad = v, capacidadError = null) }
    fun onAperturaChange(v: String) = _uiState.update { it.copy(horaApertura = v, aperturaError = null) }
    fun onCierreChange(v: String) = _uiState.update { it.copy(horaCierre = v, cierreError = null) }
    fun onEstadoChange(v: String) = _uiState.update { it.copy(estado = v) }

    fun guardar() {
        val e = _uiState.value

        val rNombre = EspacioValidator.validarNombre(e.nombre)
        val rTipo = EspacioValidator.validarTipo(e.tipo)
        val rDescripcion = EspacioValidator.validarDescripcion(e.descripcion)
        val rUbicacion = EspacioValidator.validarUbicacion(e.ubicacion)
        val rCapacidad = EspacioValidator.validarCapacidad(e.capacidad)
        val rApertura = EspacioValidator.validarApertura(e.horaApertura)
        val rCierre = EspacioValidator.validarCierre(e.horaApertura, e.horaCierre)

        _uiState.update {
            it.copy(
                nombreError = rNombre.error,
                tipoError = rTipo.error,
                descripcionError = rDescripcion.error,
                ubicacionError = rUbicacion.error,
                capacidadError = rCapacidad.error,
                aperturaError = rApertura.error,
                cierreError = rCierre.error
            )
        }

        val todoOk = listOf(rNombre, rTipo, rDescripcion, rUbicacion, rCapacidad, rApertura, rCierre).all { it.ok }
        if (!todoOk) return

        viewModelScope.launch {
            val espacio = EspacioEntity(
                id = e.id,
                nombre = e.nombre.trim(),
                tipo = e.tipo.trim(),
                descripcion = e.descripcion.trim(),
                ubicacion = e.ubicacion.trim(),
                capacidadMax = e.capacidad.trim().toInt(),
                horaApertura = e.horaApertura.trim(),
                horaCierre = e.horaCierre.trim(),
                estado = e.estado
            )
            if (e.id == 0) repository.insertar(espacio) else repository.actualizar(espacio)
            _uiState.update { it.copy(guardadoExitoso = true) }
        }
    }
}