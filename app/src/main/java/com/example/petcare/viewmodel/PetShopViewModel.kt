package com.example.petcare.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.repository.PetShopRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

data class PetShopMarker(
    val name: String,
    val geoPoint: GeoPoint
)

class PetShopViewModel(private val repository: PetShopRepository) : ViewModel() {

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val _petShops = MutableStateFlow<List<PetShopMarker>>(emptyList())
    val petShops = _petShops.asStateFlow()

    // Ponto padrão caso a localização não seja encontrada
    val initialMapPoint = GeoPoint(-23.5505, -46.6333)

    @SuppressLint("MissingPermission") // A permissão é verificada na UI
    fun startLocationUpdates(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userPoint = GeoPoint(location.latitude, location.longitude)
                    _userLocation.value = userPoint
                    // Assim que temos a localização, buscamos os pet shops próximos
                    fetchPetShopsAround(userPoint)
                } else {
                    // Se a localização falhar, pode-se carregar pet shops de um local padrão ou mostrar erro
                }
            }
            .addOnFailureListener {
                // Tratar erro ao obter localização
                it.printStackTrace()
            }
    }

    private fun fetchPetShopsAround(point: GeoPoint) {
        viewModelScope.launch {
            // A query busca pet shops e veterinários num raio de 5km (5000 metros)
            val query = """
                [out:json];
                (
                  node["shop"="pet"](around:5000,${point.latitude},${point.longitude});
                  node["amenity"="veterinary"](around:5000,${point.latitude},${point.longitude});
                );
                out;
            """.trimIndent()

            try {
                val response = repository.getPetShops(query)

                // Mapeia os elementos do JSON para a nossa lista de marcadores
                val markers = response.elements.mapNotNull { element ->
                    // Ignora elementos sem nome ou sem coordenadas válidas
                    if (element.tags?.get("name") != null && element.lat != 0.0 && element.lon != 0.0) {
                        PetShopMarker(
                            name = element.tags["name"]!!,
                            geoPoint = GeoPoint(element.lat, element.lon)
                        )
                    } else {
                        null
                    }
                }
                _petShops.value = markers

            } catch (e: Exception) {
                // Trata erros de conexão ou parsing do JSON
                e.printStackTrace()
            }
        }
    }
}
