package com.example.travelapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.TravelRequest
import com.example.travelapp.TravelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelScreen(
    modifier: Modifier = Modifier,
    vm: TravelViewModel = viewModel()
) {
    // Opciones para cada campo
    val opciones = mapOf(
        "popularidad" to listOf("alta","media","baja"),
        "estacion"    to listOf("verano","otoño","invierno","primavera"),
        "rating"      to listOf("alto","medio","bajo"),
        "precio"      to listOf("alto","medio","bajo"),
        "clima"       to listOf("tropical","templado","frío"),
        "idioma"      to listOf("inglés","español","otro"),
        "continente"  to listOf("Europa","América","Asia","África","Oceanía")
    )

    // Estados locales con la selección inicial
    val selections = remember { mutableStateMapOf<String, String>() }
    LaunchedEffect(Unit) {
        opciones.forEach { (k, vals) -> selections[k] = vals.first() }
    }

    val recs by vm.recs.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Por cada campo, un dropdown
        opciones.forEach { (label, vals) ->
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selections[label] ?: "",
                    onValueChange = { },
                    label = { Text(label.replaceFirstChar { it.uppercase() }) },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    vals.forEach { v ->
                        DropdownMenuItem(
                            text = { Text(v) },
                            onClick = {
                                selections[label] = v
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Botón para solicitar
        Button(
            onClick = {
                vm.solicitarRecomendacion(
                    TravelRequest(
                        popularidad = selections["popularidad"]!!,
                        estacion    = selections["estacion"]!!,
                        rating      = selections["rating"]!!,
                        precio      = selections["precio"]!!,
                        clima       = selections["clima"]!!,
                        idioma      = selections["idioma"]!!,
                        continente  = selections["continente"]!!
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener recomendaciones")
        }

        Spacer(Modifier.height(16.dp))

        // Mostrar resultados
        recs.forEach { destino ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation()
            ) {
                Text(destino, Modifier.padding(12.dp))
            }
        }
    }
}
