package com.dam.explora.ui.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dam.explora.R
import com.dam.explora.data.Orden
import com.dam.explora.data.lugaresIniciales
import com.dam.explora.data.ordenar
import com.dam.explora.data.pendientes
import com.dam.explora.ui.components.BuscadorLugares
import com.dam.explora.ui.components.TarjetaLugar

private const val TAG = "Explora"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploraApp() {
    var consulta by rememberSaveable { mutableStateOf("") }
    var soloPendientes by rememberSaveable { mutableStateOf(false) }
    var ordenActual by rememberSaveable { mutableStateOf(Orden.NOMBRE) }
    var lugares by remember { mutableStateOf(lugaresIniciales) }

    val lugaresVisibles = lugares
        .let { lista -> if (soloPendientes) lista.pendientes() else lista }
        .filter {
            it.nombre.contains(consulta, ignoreCase = true) ||
                    it.municipio.contains(consulta, ignoreCase = true)
        }
        .ordenar(ordenActual)

    val totalVisitados = lugares.count { it.visitado }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BuscadorLugares(
                consulta = consulta,
                onConsultaChange = { consulta = it },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { soloPendientes = !soloPendientes }
            ) {
                Checkbox(
                    checked = soloPendientes,
                    onCheckedChange = { soloPendientes = it }
                )
                Text(
                    text = stringResource(R.string.solo_pendientes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Orden.entries.forEach { criterio ->
                    Button(
                        onClick = {
                            ordenActual = criterio
                            Log.i(
                                TAG,
                                "Orden cambiado a ${criterio.name}. Lugares mostrados: ${lugaresVisibles.size}"
                            )
                        },
                        enabled = ordenActual != criterio,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(criterio.etiqueta))
                    }
                }
            }

            Text(
                text = pluralStringResource(
                    id = R.plurals.contador_visitados_plural,
                    count = totalVisitados,
                    totalVisitados,
                    lugares.size
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = pluralStringResource(
                    id = R.plurals.mostrando_lugares,
                    count = lugaresVisibles.size,
                    lugaresVisibles.size
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (lugaresVisibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = stringResource(R.string.sin_lugares),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lugaresVisibles, key = { it.id }) { lugar ->
                        TarjetaLugar(
                            lugar = lugar,
                            onVisitadoChange = { nuevoEstado ->
                                lugares = lugares.map { item ->
                                    if (item.id == lugar.id) item.copy(visitado = nuevoEstado) else item
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "1. Español (Claro)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "2. Español (Oscuro)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "3. Valencià (CA)", showBackground = true, locale = "ca")
@Preview(name = "4. English (EN)", showBackground = true, locale = "en")
@Preview(name = "5. Fuente Grande", showBackground = true, fontScale = 1.5f)
@Preview(name = "6. Tablet", showBackground = true, device = Devices.PIXEL_TABLET)
@Composable
fun ExploraAppPreview() {
    val colores = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    MaterialTheme(colorScheme = colores) {
        ExploraApp()
    }
}