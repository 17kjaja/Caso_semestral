package com.example.reservasespacios.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.ui.components.CampoFormulario
import com.example.reservasespacios.viewmodel.NuevaReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaReservaScreen(
    espacioId: Int,
    onVolver: () -> Unit,
    viewModel: NuevaReservaViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(espacioId) { viewModel.cargarEspacio(espacioId) }

    LaunchedEffect(estado.guardadoExitoso) {
        if (estado.guardadoExitoso) {
            Toast.makeText(context, "Solicitud enviada", Toast.LENGTH_SHORT).show()
            onVolver()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva solicitud") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            estado.espacio?.let { espacio ->
                Text(espacio.nombre, style = MaterialTheme.typography.titleLarge)
                Text(
                    "Capacidad máxima: ${espacio.capacidadMax} · Horario: ${espacio.horaApertura} a ${espacio.horaCierre}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CampoFormulario(
                value = estado.actividad,
                onValueChange = viewModel::onActividadChange,
                label = "Actividad",
                icono = Icons.Default.Edit,
                error = estado.actividadError
            )
            CampoFormulario(
                value = estado.cursoGrupo,
                onValueChange = viewModel::onCursoChange,
                label = "Curso o grupo",
                icono = Icons.Default.School,
                error = estado.cursoError
            )
            CampoFormulario(
                value = estado.asistentes,
                onValueChange = viewModel::onAsistentesChange,
                label = "Cantidad de asistentes",
                icono = Icons.Default.Group,
                error = estado.asistentesError,
                teclado = KeyboardType.Number
            )
            CampoFormulario(
                value = estado.fecha,
                onValueChange = viewModel::onFechaChange,
                label = "Fecha (AAAA-MM-DD)",
                icono = Icons.Default.Event,
                error = estado.fechaError
            )
            CampoFormulario(
                value = estado.horaInicio,
                onValueChange = viewModel::onHoraInicioChange,
                label = "Hora de inicio (HH:mm)",
                icono = Icons.Default.Schedule,
                error = estado.horaInicioError
            )
            CampoFormulario(
                value = estado.horaFin,
                onValueChange = viewModel::onHoraFinChange,
                label = "Hora de término (HH:mm)",
                icono = Icons.Default.Schedule,
                error = estado.horaFinError
            )
            CampoFormulario(
                value = estado.observaciones,
                onValueChange = viewModel::onObservacionesChange,
                label = "Observaciones (opcional)",
                icono = Icons.Default.Notes,
                error = null,
                unaLinea = false
            )

            estado.errorGeneral?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::onEnviarClick,
                enabled = !estado.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Enviar solicitud")
                }
            }
        }
    }

    if (estado.mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = viewModel::cancelarConfirmacion,
            title = { Text("Confirmar solicitud") },
            text = {
                Text(
                    "¿Enviar la solicitud para ${estado.espacio?.nombre} el ${estado.fecha} " +
                            "de ${estado.horaInicio} a ${estado.horaFin}?"
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarEnvio) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelarConfirmacion) { Text("Cancelar") }
            }
        )
    }
}