# Documentación de cambios — ExploraGym

## 1. Archivos creados y modificados

Para mejorar la organización y el mantenimiento del proyecto (anteriormente todo el código residía en `MainActivity.kt`), se ha separado la aplicación en los paquetes `data`, `ui/components` y `ui/screens` dentro de `com.dam.explora`:

```text
com.dam.explora
├── MainActivity.kt
├── data
│   ├── Lugar.kt
│   ├── Orden.kt
│   └── DatosIniciales.kt
└── ui
    ├── components
    │   ├── BuscadorLugares.kt
    │   └── TarjetaLugar.kt
    └── screens
        └── ExploraApp.kt
```

### Detalle de archivos
- **`data/Lugar.kt` (creado):** contiene el modelo de datos (`Dificultad`, `TipoReto`, `Reto` y `Lugar`).
  - *Adaptación del criterio numérico:* el modelo de `ExploraGym` no dispone de un campo `puntos` y todos los gimnasios tienen el mismo número de retos (2). Por ello, se ha añadido la propiedad numérica `nivel: Int` al enum `Dificultad` (`PRINCIPIANTE = 1`, `INTERMEDIO = 2`, `AVANZADO = 3`) para permitir la ordenación de mayor a menor dificultad de forma equivalente.
- **`data/Orden.kt` (creado):** define la `enum class Orden(@StringRes val etiqueta: Int)` con los criterios `NOMBRE` y `DIFICULTAD`, junto a las funciones de extensión:
  - `List<Lugar>.ordenar(orden: Orden)`: usa un bloque `when` exhaustivo sin rama `else` (alfabéticamente por nombre y de mayor a menor por `dificultadPrincipal.nivel`).
  - `List<Lugar>.pendientes()`: filtra únicamente los lugares con `visitado == false`.
- **`data/DatosIniciales.kt` (creado):** almacena los datos iniciales `retosIniciales` y `lugaresIniciales`.
- **`ui/components/BuscadorLugares.kt` (creado):** componente reutilizable con el `OutlinedTextField` de búsqueda por nombre o municipio.
- **`ui/components/TarjetaLugar.kt` (creado):** componente reutilizable que muestra cada gimnasio, su dificultad traducida y la casilla de visitado.
- **`ui/screens/ExploraApp.kt` (creado):** pantalla principal de la aplicación. Incorpora:
  - La casilla (`Checkbox`) **«Solo pendientes»**, combinada con la búsqueda por texto.
  - Un botón por cada criterio de `Orden`, manteniendo desactivado (`enabled = false`) el botón del criterio activo.
  - El contador **«Mostrando N lugares»** mediante `pluralStringResource` y un mensaje informativo cuando la lista filtrada está vacía.
  - Registro en Logcat con `Log.i` bajo la etiqueta `"Explora"` cada vez que cambia el criterio de orden, indicando el criterio seleccionado y el número de lugares visibles.
  - Las `@Preview` de la pantalla, incluida la de fuente grande (`fontScale = 1.5f`).
- **`MainActivity.kt` (modificado):** se ha simplificado para mantener únicamente el punto de entrada (`setContent`) y los registros de ciclo de vida (`Log.d`), delegando la interfaz en `ExploraApp()`.
- **`res/values/strings.xml`, `res/values-ca/strings.xml` y `res/values-en/strings.xml` (modificados):** añadidos todos los textos nuevos, los nombres de los criterios de orden (`orden_nombre`, `orden_dificultad`), el mensaje de estado vacío (`sin_lugares`) y el recurso `<plurals name="mostrando_lugares">` en los tres idiomas de la app (español, valenciano e inglés).

---

## 2. Por qué la lista mostrada se calcula a partir del estado

La lista `lugaresVisibles` se calcula dinámicamente en cada recomposición aplicando sobre `lugares` el filtro de pendientes, la consulta de búsqueda y el criterio de orden, en lugar de guardarse en un `mutableStateOf` propio. Esto se hace por los siguientes motivos:

1. **Única fuente de verdad (*Single Source of Truth*):** los datos reales viven en una sola lista (`lugares`) y las opciones del usuario en sus respectivos estados (`consulta`, `soloPendientes`, `ordenActual`).
2. **Evitar desincronizaciones:** si la lista visible fuese un estado independiente, habría que actualizarla manualmente cada vez que cambiara la búsqueda, el filtro, el orden o cualquier casilla de `visitado`, lo que fácilmente provocaría inconsistencias.
3. **Claridad en el flujo unidireccional de datos:** en Jetpack Compose, un estado derivado se recalcula de forma determinista y reactiva cada vez que cambia cualquiera de los estados de los que depende.

---

## 3. Qué se conserva y qué se pierde al girar la pantalla, y por qué

Cuando se gira el emulador o dispositivo, Android detecta un cambio de configuración y **destruye y vuelve a crear `MainActivity`** (ejecutándose de nuevo el ciclo de vida hasta `onDestroy` y posteriormente `onCreate`).

- **Se conservan:**
  - El texto de **búsqueda** (`consulta: String`).
  - El **filtro** de «Solo pendientes» (`soloPendientes: Boolean`).
  - El **criterio de orden** (`ordenActual: Orden`).
  - **Por qué:** están declarados con `rememberSaveable`, que guarda automáticamente sus valores en el `Bundle` de estado de la actividad (`String`, `Boolean` y `enum` son serializables de forma nativa por Android) y los restaura tras la recreación.
- **Se pierden:**
  - Los cambios realizados en las **casillas de visitado** de cada gimnasio.
  - **Por qué:** la lista `lugares` está almacenada con `remember` (no con `rememberSaveable` ni en un `ViewModel`). `remember` solo conserva el estado en memoria durante las recomposiciones de la actividad actual, por lo que al destruirse y recrearse `MainActivity` vuelve a inicializarse con los valores por defecto de `lugaresIniciales`.

---

## 4. Configuración de SDK (`minSdk` y `targetSdk`)

La configuración definida en `app/build.gradle.kts` (`minSdk = 26`, `targetSdk = 36`, `compileSdk = 36`) responde a los siguientes criterios técnicos:

- **`minSdk = 26` (Android 8.0 Oreo):** cubre aproximadamente el **99,0 %** de los dispositivos Android activos según los datos de distribución de Google (octubre de 2026). Mantener API 26 como versión mínima permite disponer de forma nativa de iconos adaptativos (`adaptive-icon`), canales de notificación, mejoras en la gestión de procesos en segundo plano y las APIs modernas de Java 8 (`java.time`) sin necesidad de *desugaring*, ofreciendo soporte completo para Jetpack Compose y Material 3.
- **`targetSdk = 36` (Android 16):** se configura en el nivel de API más reciente para asegurar que la aplicación está probada y adaptada a los cambios actuales del sistema en materia de seguridad, privacidad y renderizado moderno (como la visualización *edge-to-edge*). Además, a partir del 31 de agosto de 2026 Google Play exige que todas las aplicaciones nuevas y actualizaciones apunten a API 36 o superior, sin que esto afecte a la retrocompatibilidad con dispositivos desde `minSdk = 26`.

---
