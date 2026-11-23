package com.dam.explora.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dam.explora.R
import com.dam.explora.data.Dificultad
import com.dam.explora.data.Lugar

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