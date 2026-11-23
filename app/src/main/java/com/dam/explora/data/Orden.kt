package com.dam.explora.data

import androidx.annotation.StringRes
import com.dam.explora.R

enum class Orden(@StringRes val etiqueta: Int) {
    NOMBRE(R.string.orden_nombre),
    DIFICULTAD(R.string.orden_dificultad)
}

fun List<Lugar>.ordenar(orden: Orden): List<Lugar> = when (orden) {
    Orden.NOMBRE -> sortedBy { it.nombre.lowercase() }
    Orden.DIFICULTAD -> sortedByDescending { it.dificultadPrincipal.nivel }
}

fun List<Lugar>.pendientes(): List<Lugar> = filter { !it.visitado }