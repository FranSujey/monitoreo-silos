package com.example.monitoreo_silos.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.monitoreo_silos.R

class NotificationHelper(private val context: Context) {
    private val channelId = "silos_alerts_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alertas de Silos"
            val descriptionText = "Notificaciones de nivel crítico de alimento"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun enviarAlertaSiloCritico(siloId: Int, nombreGranja: String, nivelActual: Double) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("⚠️ ALERTA CRÍTICA: Silo #$siloId")
            .setContentText("En $nombreGranja el nivel bajó a ${nivelActual.toInt()} kg (Menos del 20%)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(siloId, builder.build())
    }
}