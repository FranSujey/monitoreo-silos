package com.example.monitoreo_silos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monitoreo_silos.data.MockData
import com.example.monitoreo_silos.model.EstadoSilo
import com.example.monitoreo_silos.model.Silo
import com.example.monitoreo_silos.ui.theme.MonitoreoSilosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonitoreoSilosTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SilosScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SilosScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Monitoreo de Silos", fontWeight = FontWeight.Bold) }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(MockData.silosDePrueba) { silo: Silo ->
                SiloCard(silo = silo)
            }
        }
    }
}

@Composable
fun SiloCard(silo: Silo) {
    val (colorEstado, textoEstado) = when (silo.estado) {
        EstadoSilo.NORMAL -> Color(0xFF2E7D32) to "Normal"
        EstadoSilo.ADVERTENCIA -> Color(0xFFFFEB3B) to "Advertencia"
        EstadoSilo.CRITICO -> Color(0xFFC62828) to "Crítico"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = silo.id,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = textoEstado,
                        color = colorEstado,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Text(
                text = silo.nombreGranja,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nivel: ${silo.nivelActualKg.toInt()} / ${silo.capacidadTotalKg.toInt()} kg",
                    fontSize = 14.sp
                )
                Text(
                    text = "${silo.porcentajeDisponible}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { silo.porcentajeDisponible / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = colorEstado,
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Actualizado: ${silo.fechaUltimaActualizacion}",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SilosScreenPreview() {
    MonitoreoSilosTheme {
        SilosScreen()
    }
}