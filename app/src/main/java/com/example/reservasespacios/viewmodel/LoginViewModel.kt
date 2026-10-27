package com.example.reservasespacios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservasespacios.data.local.AppDatabase
import com.example.reservasespacios.data.repository.UsuarioRepository
import com.example.reservasespacios.domain.validation.LoginValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val usuarioError: String? = null,
    val claveError: String? = null,
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val perfilLogueado: String? = null
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository(
        AppDatabase.obtener(application).usuarioDao()
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        _uiState.update { it.copy(usuario = valor, usuarioError = null, errorGeneral = null) }
    }

    fun onClaveChange(valor: String) {
        _uiState.update { it.copy(clave = valor, claveError = null, errorGeneral = null) }
    }

    fun iniciarSesion() {
        val estado = _uiState.value
        val rUsuario = LoginValidator.validarUsuario(estado.usuario)
        val rClave = LoginValidator.validarClave(estado.clave)

        if (!rUsuario.ok || !rClave.ok) {
            _uiState.update { it.copy(usuarioError = rUsuario.error, claveError = rClave.error) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val usuario = repository.iniciarSesion(estado.usuario.trim(), estado.clave)
            if (usuario == null) {
                _uiState.update { it.copy(cargando = false, errorGeneral = "Usuario o clave incorrectos") }
            } else {
                _uiState.update { it.copy(cargando = false, perfilLogueado = usuario.perfil) }
            }
        }
    }

    fun consumirLogin() {
        _uiState.update { it.copy(perfilLogueado = null) }
    }
}