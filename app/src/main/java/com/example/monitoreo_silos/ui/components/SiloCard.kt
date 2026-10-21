package com.example.monitoreo_silos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.monitoreo_silos.data.local.SiloEntity

@Composable
fun SiloCard(
    silo: SiloEntity,
    onSiloClick: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    // Determinar color según reglas de negocio del caso
    val colorEstado = when {
        silo.porcentajeLlenado > 30.0 -> Color(0xFF2E7D32)      // Verde (Normal)
        silo.porcentajeLlenado in 20.0..30.0 -> Color(0xFFF57C00) // Amarillo/Naranja (Advertencia)
        else -> Color(0xFFC62828)                              // Rojo (Crítico)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expandido = !expandido },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Silo #${silo.id} - ${silo.tipoAlimento}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Granja ID: ${silo.granjaId}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Insignia/Badge de Estado
                Surface(
                    color = colorEstado,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "${silo.porcentajeLlenado.toInt()}% (${silo.estado})",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Animación para mostrar/ocultar información extendida y alertas
            AnimatedVisibility(
                visible = expandido,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Capacidad Total: ${silo.capacidadTotal} Toneladas")
                    Text(text = "Cantidad Disponible: ${silo.cantidadDisponible} Toneladas")

                    if (silo.porcentajeLlenado < 20.0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "⚠️ ALERTA: Nivel crítico. Requiere reposición urgente.",
                                color = Color(0xFFC62828),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

