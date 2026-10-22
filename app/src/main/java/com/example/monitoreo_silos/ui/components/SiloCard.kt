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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monitoreo_silos.data.local.SiloEntity

@Composable
fun SiloCard(
    silo: SiloEntity,
    onSiloClick: (SiloEntity) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    // Color semántico según el nivel del caso
    val colorEstado = when {
        silo.porcentajeLlenado > 30.0 -> Color(0xFF2E7D32)      // Verde (Normal)
        silo.porcentajeLlenado in 20.0..30.0 -> Color(0xFFF57C00) // Naranja (Advertencia)
        else -> Color(0xFFC62828)                              // Rojo (Crítico)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expandido = !expandido },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera del Silo y Badge de Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SILO-${String.format("%03d", silo.id)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tipo: ${silo.tipoAlimento}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Insignia/Badge
                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${silo.porcentajeLlenado.toInt()}% (${silo.estado})",
                        color = colorEstado,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Indicador numérico de Nivel y Barra de Progreso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nivel: ${silo.cantidadDisponible.toInt()} / ${silo.capacidadTotal.toInt()} kg",
                    fontSize = 14.sp
                )
                Text(
                    text = "${silo.porcentajeLlenado.toInt()}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (silo.porcentajeLlenado / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = colorEstado,
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de acción para abrir el Formulario
            Button(
                onClick = { onSiloClick(silo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Movimiento / Incidencia")
            }

            // Animaciones de detalles desplegables
            AnimatedVisibility(
                visible = expandido,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Granja ID: ${silo.granjaId}")
                    Text(text = "Capacidad Total: ${silo.capacidadTotal} kg")
                    Text(text = "Cantidad Disponible: ${silo.cantidadDisponible} kg")

                    if (silo.porcentajeLlenado < 20.0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "⚠️ ALERTA: Nivel crítico. Requiere reposición urgente.",
                                color = Color(0xFFC62828),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}