package com.dam.explora.data

import com.dam.explora.R

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