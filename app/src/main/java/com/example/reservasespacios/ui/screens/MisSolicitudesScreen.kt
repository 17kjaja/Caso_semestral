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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.data.local.ReservaConDetalle
import com.example.reservasespacios.ui.components.EstadoChip
import com.example.reservasespacios.viewmodel.MisSolicitudesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisSolicitudesScreen(
    onVolver: () -> Unit,
    viewModel: MisSolicitudesViewModel = viewModel()
) {
    val reservas by viewModel.reservas.collectAsState()
    val estado by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis solicitudes") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (reservas.isEmpty()) {
            Text(
                "Aún no tienes solicitudes",
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
                items(reservas) { item ->
                    SolicitudCard(item, onCancelar = { viewModel.pedirCancelar(item) })
                }
            }
        }
    }

    estado.reservaACancelar?.let { item ->
        AlertDialog(
            onDismissRequest = viewModel::descartarCancelar,
            title = { Text("Cancelar solicitud") },
            text = { Text("¿Cancelar la reserva de ${item.espacioNombre} el ${item.reserva.fecha}?") },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarCancelar) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::descartarCancelar) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun SolicitudCard(item: ReservaConDetalle, onCancelar: () -> Unit) {
    val r = item.reserva
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.espacioNombre, style = MaterialTheme.typography.titleMedium)
                EstadoChip(r.estado)
            }
            Text("${r.fecha} · ${r.horaInicio} a ${r.horaFin}", style = MaterialTheme.typography.bodyMedium)
            Text("${r.actividad} (${r.cursoGrupo}, ${r.asistentes} asistentes)", style = MaterialTheme.typography.bodySmall)

            if (r.estado == "RECHAZADA" && r.justificacion.isNotBlank()) {
                Text(
                    "Motivo: ${r.justificacion}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (r.estado == "PENDIENTE" || r.estado == "ACEPTADA") {
                OutlinedButton(onClick = onCancelar, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Cancelar")
                }
            }
        }
    }
}