package com.example.reservasespacios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.ui.components.CampoFormulario
import com.example.reservasespacios.ui.components.SolicitarPermisoNotificaciones
import com.example.reservasespacios.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onCerrarSesion: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val pendientes by viewModel.pendientes.collectAsState()
    val estado by viewModel.uiState.collectAsState()

    SolicitarPermisoNotificaciones()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes pendientes") },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        if (pendientes.isEmpty()) {
            Text(
                "No hay solicitudes pendientes",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pendientes) { item ->
                    PendienteCard(
                        item = item,
                        onAprobar = { viewModel.pedirAprobar(item) },
                        onRechazar = { viewModel.pedirRechazar(item) }
                    )
                }
            }
        }
    }

    estado.reservaAAprobar?.let { item ->
        AlertDialog(
            onDismissRequest = viewModel::descartar,
            title = { Text("Aprobar solicitud") },
            text = { Text("¿Aprobar la reserva de ${item.espacioNombre} el ${item.reserva.fecha}?") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarAprobar) { Text("Aprobar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::descartar) { Text("Volver") }
            }
        )
    }

    estado.reservaARechazar?.let { item ->
        AlertDialog(
            onDismissRequest = viewModel::descartar,
            title = { Text("Rechazar solicitud") },
            text = {
                Column {
                    Text("Indica el motivo del rechazo de ${item.espacioNombre}:")
                    CampoFormulario(
                        value = estado.justificacion,
                        onValueChange = viewModel::onJustificacionChange,
                        label = "Justificación",
                        icono = Icons.Default.Edit,
                        error = estado.justificacionError,
                        unaLinea = false
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarRechazar) { Text("Rechazar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::descartar) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun PendienteCard(
    item: ReservaConDetalle,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit
) {
    val r = item.reserva
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(item.espacioNombre, style = MaterialTheme.typography.titleMedium)
            Text("Solicita: ${item.solicitanteNombre}", style = MaterialTheme.typography.bodyMedium)
            Text("${r.fecha} · ${r.horaInicio} a ${r.horaFin}", style = MaterialTheme.typography.bodyMedium)
            Text("${r.actividad} (${r.cursoGrupo}, ${r.asistentes} asistentes)", style = MaterialTheme.typography.bodySmall)

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onAprobar) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(" Aprobar")
                }
                OutlinedButton(onClick = onRechazar) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Text(" Rechazar")
                }
            }
        }
    }
}