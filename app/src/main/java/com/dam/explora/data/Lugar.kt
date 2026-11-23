package com.dam.explora.data

import androidx.annotation.DrawableRes

enum class Dificultad(val nivel: Int) {
    PRINCIPIANTE(1),
    INTERMEDIO(2),
    AVANZADO(3)
}

sealed interface TipoReto {
    data class Fuerza(val series: Int, val repeticiones: Int, val pesoKg: Int) : TipoReto
    data class Cardio(val minutos: Int, val distanciaKm: Double) : TipoReto
    data class Funcional(val rondas: Int, val ejercicio: String) : TipoReto
}

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