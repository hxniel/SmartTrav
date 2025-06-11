package com.example.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.travelapp.ui.SequentialInputScreen
import com.example.travelapp.ui.ResultsScreen
import com.example.travelapp.ui.theme.TravelAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAppTheme {
                val vm: TravelViewModel = remember { TravelViewModel() }
                var inputs by remember { mutableStateOf<Map<String,String>?>(null) }

                if (inputs == null) {
                    // Muestra la secuencia de inputs
                    SequentialInputScreen(modifier = Modifier.fillMaxSize()) { resultMap ->
                        inputs = resultMap
                        // Llamar al backend
                        vm.solicitarRecomendacion(
                            TravelRequest(
                                popularidad = resultMap["popularidad"]!!,
                                estacion    = resultMap["estación"]!!,
                                rating      = resultMap["rating"]!!,
                                precio      = resultMap["precio"]!!,
                                clima       = resultMap["clima"]!!,
                                idioma      = resultMap["idioma"]!!,
                                continente  = resultMap["continente"]!!
                            )
                        )
                    }
                } else {
                    // Muestra resultados o loader
                    ResultsScreen(
                        recs = vm.recs.collectAsState().value,
                        loading = vm.loading.collectAsState().value,
                        onReset = { inputs = null }
                    )
                }
            }
        }
    }
}