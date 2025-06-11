package com.example.travelapp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//pantalla que muestra categorias una x una
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SequentialInputScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    onComplete: (Map<String, String>) -> Unit
) {
    // Categorías y sus opciones
    val steps = listOf(
        "popularidad" to listOf("alta", "media", "baja"),
        "estación"    to listOf("primavera", "verano", "otoño", "invierno"),
        "rating"      to listOf("alto", "medio", "bajo"),
        "precio"      to listOf("alto", "medio", "bajo"),
        "clima"       to listOf(
            "alpino", "continental", "desértico", "mediterráneo", "oceánico", "semíárido",
            "subtropical", "subártico", "templado", "templado húmedo", "tropical", "árido"
        ),
        "idioma"      to listOf(
            "español", "inglés", "francés", "alemán", "italiano", "ruso", "chino",
            "japonés", "portugués", "árabe", "neerlandés", "sueco", "noruego", "polaco",
            "turco", "tailandés", "vietnamita", "laosiano", "suajili"
        ),
        "continente"  to listOf("América", "Europa", "Asia", "África", "Oceanía")
    )

    var stepIndex by remember { mutableStateOf(0) }
    val results = remember { mutableStateMapOf<String, String>() }

    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Smart Travel",
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            AnimatedContent(
                targetState = stepIndex,
                transitionSpec = {
                    slideInHorizontally(tween(300)) with slideOutHorizontally(tween(300))
                }
            ) { idx ->
                val (label, options) = steps[idx]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = label.uppercase(),
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    options.forEach { option ->
                        GradientButton(
                            text = option,
                            onClick = {
                                results[label] = option
                                if (stepIndex < steps.lastIndex) {
                                    stepIndex++
                                } else {
                                    onComplete(results)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Botón con degradado y esquinas redondeadas.
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFA709A), Color(0xFFFE5426))
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
