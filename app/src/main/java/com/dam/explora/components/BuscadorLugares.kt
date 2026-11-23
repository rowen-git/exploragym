package com.dam.explora.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dam.explora.R

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