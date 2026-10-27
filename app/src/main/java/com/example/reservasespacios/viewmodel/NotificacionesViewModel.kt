package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.NotificacionEntity
import com.example.reservasespacios.data.repository.NotificacionRepository
import com.example.reservasespacios.util.SesionActual
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificacionesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NotificacionRepository(AppDatabase.obtener(application).notificacionDao())

    val notificaciones: StateFlow<List<NotificacionEntity>> = repository
        .obtenerPorUsuario(SesionActual.usuarioId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val noLeidas: StateFlow<Int> = repository
        .contarNoLeidas(SesionActual.usuarioId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun marcarTodasLeidas() {
        viewModelScope.launch { repository.marcarTodasLeidas(SesionActual.usuarioId) }
    }
}