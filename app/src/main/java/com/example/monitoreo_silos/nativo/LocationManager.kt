package com.example.monitoreo_silos.nativo

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationManager(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun obtenerUbicacionActual(
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                onSuccess(location.latitude, location.longitude)
            } else {
                onError(Exception("No se pudo obtener la ubicación actual."))
            }
        }.addOnFailureListener { exception ->
            onError(exception)
        }
    }
}