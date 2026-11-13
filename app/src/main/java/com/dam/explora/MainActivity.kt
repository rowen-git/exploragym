package com.dam.explora

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme

// enum de dificultad
enum class Dificultad {
    PRINCIPIANTE, INTERMEDIO, AVANZADO
}

// tipo de desafio
sealed interface TipoReto {
    data class Fuerza(val series: Int, val repeticiones: Int, val pesoKg: Int) : TipoReto
    data class Cardio(val minutos: Int, val distanciaKm: Double) : TipoReto
    data class Funcional(val rondas: Int, val ejercicio: String) : TipoReto
}

// 3. Modelo de dados: data class para Reto e Lugar
data class Reto(
    val id: Int,
    val nombre: String,
    val dificultad: Dificultad,
    val tipo: TipoReto,
    val completado: Boolean = false
)

data class Lugar(
    val id: Int,
    val nombre: String,
    val municipio: String,
    @DrawableRes val imagenRes: Int,
    val dificultadPrincipal: Dificultad,
    val retos: List<Reto>,
    val visitado: Boolean = false
)

// desafios
val retosIniciales = listOf(
    Reto(1, "Press de banca", Dificultad.INTERMEDIO, TipoReto.Fuerza(4, 10, 60)),
    Reto(2, "Sentadilla libre", Dificultad.AVANZADO, TipoReto.Fuerza(5, 5, 100)),
    Reto(3, "Carrera en cinta HIIT", Dificultad.PRINCIPIANTE, TipoReto.Cardio(20, 3.5)),
    Reto(4, "Remo indoor", Dificultad.INTERMEDIO, TipoReto.Cardio(15, 3.0)),
    Reto(5, "Circuito CrossFit WOD", Dificultad.AVANZADO, TipoReto.Funcional(5, "Burpees y Kettlebell")),
    Reto(6, "Salto al cajón", Dificultad.INTERMEDIO, TipoReto.Funcional(4, "Box Jumps")),
    Reto(7, "Peso muerto", Dificultad.AVANZADO, TipoReto.Fuerza(4, 8, 120)),
    Reto(8, "Dominadas estrictas", Dificultad.INTERMEDIO, TipoReto.Funcional(4, "Pull-ups")),
    Reto(9, "Bicicleta Spinning", Dificultad.PRINCIPIANTE, TipoReto.Cardio(45, 18.0)),
    Reto(10, "Plancha abdominal", Dificultad.PRINCIPIANTE, TipoReto.Funcional(3, "Core 60s"))
)

val lugaresIniciales = listOf(
    Lugar(
        id = 1,
        nombre = "Gimnasio Iron Fitness",
        municipio = "Valencia Centro",
        imagenRes = R.drawable.ic_launcher_foreground,
        dificultadPrincipal = Dificultad.AVANZADO,
        retos = listOf(retosIniciales[0], retosIniciales[1]),
        visitado = true
    ),
    Lugar(
        id = 2,
        nombre = "Gimnasio CardioZone",
        municipio = "Ruzafa",
        imagenRes = R.drawable.ic_launcher_foreground,
        dificultadPrincipal = Dificultad.PRINCIPIANTE,
        retos = listOf(retosIniciales[2], retosIniciales[3]),
        visitado = false
    ),
    Lugar(
        id = 3,
        nombre = "CrossBox Titanes",
        municipio = "Benimaclet",
        imagenRes = R.drawable.ic_launcher_foreground,
        dificultadPrincipal = Dificultad.AVANZADO,
        retos = listOf(retosIniciales[4], retosIniciales[5]),
        visitado = false
    ),
    Lugar(
        id = 4,
        nombre = "PowerGym Olimpo",
        municipio = "Campanar",
        imagenRes = R.drawable.ic_launcher_foreground,
        dificultadPrincipal = Dificultad.INTERMEDIO,
        retos = listOf(retosIniciales[6], retosIniciales[7]),
        visitado = false
    ),
    Lugar(
        id = 5,
        nombre = "Studio Fit & Spin",
        municipio = "El Carmen",
        imagenRes = R.drawable.ic_launcher_foreground,
        dificultadPrincipal = Dificultad.PRINCIPIANTE,
        retos = listOf(retosIniciales[8], retosIniciales[9]),
        visitado = false
    )
)

class MainActivity : ComponentActivity() {
    private val TAG = "CicloVida"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate ejecutado")
        setContent {
            val colores = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            MaterialTheme(colorScheme = colores) {
                ExploraApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart ejecutado")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume ejecutado")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause ejecutado")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop ejecutado")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy ejecutado")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploraApp() {
    var consulta by rememberSaveable { mutableStateOf("") }
    var lugares by remember { mutableStateOf(lugaresIniciales) }

    val lugaresFiltrados = lugares.filter {
        it.nombre.contains(consulta, ignoreCase = true) ||
                it.municipio.contains(consulta, ignoreCase = true)
    }
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BuscadorLugares(
                consulta = consulta,
                onConsultaChange = { nuevaConsulta -> consulta = nuevaConsulta },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // plurals
            Text(
                text = pluralStringResource(
                    id = R.plurals.contador_visitados_plural,
                    count = totalVisitados,
                    totalVisitados,
                    lugares.size
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lugaresFiltrados, key = { it.id }) { lugar ->
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

@Composable
fun BuscadorLugares(
    consulta: String,
    onConsultaChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = consulta,
        onValueChange = onConsultaChange,
        label = { Text(text = stringResource(R.string.buscar_lugar)) },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun TarjetaLugar(
    lugar: Lugar,
    onVisitadoChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val textoDificultad = when (lugar.dificultadPrincipal) {
        Dificultad.PRINCIPIANTE -> stringResource(R.string.dificultad_principiante)
        Dificultad.INTERMEDIO -> stringResource(R.string.dificultad_intermedio)
        Dificultad.AVANZADO -> stringResource(R.string.dificultad_avanzado)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = lugar.imagenRes),
                contentDescription = lugar.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF7B61FF), Color(0xFFE86F9A))
                        )
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lugar.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = lugar.municipio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(
                        R.string.retos_disponibles,
                        lugar.retos.size,
                        textoDificultad
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Checkbox(
                checked = lugar.visitado,
                onCheckedChange = onVisitadoChange
            )
        }
    }
}

// 5. Previews: Claro, Escuro, Valenciano, Inglês, Fuente Grande e Tablet
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