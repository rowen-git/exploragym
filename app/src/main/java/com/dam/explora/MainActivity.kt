package com.dam.explora

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.dam.explora.ui.screens.ExploraApp

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