package ru.kostyakilroy.drony.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.kostyakilroy.drony.domain.location.LocationProvider
import ru.kostyakilroy.drony.domain.location.SimpleLocation
import ru.kostyakilroy.drony.domain.util.Resource
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationProvider @Inject constructor(
    private val app: Application,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationProvider {

    override suspend fun getCurrentLocation(): Resource<SimpleLocation> {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            app,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            app,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        when {
            !hasAccessCoarseLocationPermission -> {
                return Resource.Error("Нет разрешения на получение приблизительного местоположения")
            }
            !hasAccessFineLocationPermission -> {
                return Resource.Error("Нет разрешения на получение точного местоположения")
            }
            !isGpsEnabled -> return Resource.Error("Проверьте подключение к интернету или GPS")

            else -> return suspendCancellableCoroutine { cont ->
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null
                )
                    .apply {
                        if (isComplete) {
                            if (isSuccessful) {
                                cont.resume(Resource.Success(data = result.toSimpleLocation()))
                            } else {
                                cont.resume(Resource.Error(message = "Не удалось получить текущее местоположение"))
                            }
                            return@suspendCancellableCoroutine
                        }
                        addOnSuccessListener { location ->
                            cont.resume(Resource.Success(data = location.toSimpleLocation()))
                        }
                        addOnFailureListener { error ->
                            cont.resume(Resource.Error(message = error.message))
                        }
                        addOnCanceledListener {
                            cont.cancel()
                        }
                    }
            }
        }

    }

    private fun Location.toSimpleLocation(): SimpleLocation {
        return SimpleLocation(
            latitude = this.latitude,
            longitude = this.longitude
        )
    }
}