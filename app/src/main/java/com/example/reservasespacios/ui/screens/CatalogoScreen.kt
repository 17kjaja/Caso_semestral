package com.example.reservasespacios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reservasespacios.data.local.EspacioEntity
import com.example.reservasespacios.viewmodel.EspacioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    onReservar: (Int) -> Unit,
    onMisSolicitudes: () -> Unit,
    viewModel: EspacioViewModel = viewModel()
) {
    val espacios by viewModel.espacios.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Espacios disponibles") },
                actions = {
                    IconButton(onClick = onMisSolicitudes) {
                        Icon(Icons.Default.History, contentDescription = "Mis solicitudes")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(espacios) { espacio ->
                EspacioCard(espacio, onReservar = { onReservar(espacio.id) })
            }
        }
    }
}

@Composable
fun EspacioCard(espacio: EspacioEntity, onReservar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = espacio.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = espacio.descripcion, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${espacio.ubicacion} · Capacidad: ${espacio.capacidadMax}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Horario: ${espacio.horaApertura} a ${espacio.horaCierre}",
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                onClick = onReservar,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Solicitar")
            }
        }
    }
}