package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.data.repository.EspacioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EspacioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EspacioRepository(
        AppDatabase.obtener(application).espacioDao()
    )

    val espacios: StateFlow<List<EspacioEntity>> = repository.espacios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}