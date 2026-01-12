package com.example.petcare.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView as OSMapView

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onMapViewReady: (OSMapView) -> Unit
) {
    val context = LocalContext.current

    // Cria e lembra da instância do MapView
    val mapView = remember {
        OSMapView(context).apply {
            // Configurações iniciais do mapa
            setTileSource(TileSourceFactory.MAPNIK) // Fonte dos "azulejos" do mapa
            setMultiTouchControls(true) // Habilita zoom com os dedos
            controller.setZoom(15.0) // Zoom inicial
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) { view ->
        // Callback para notificar que o mapa está pronto
        onMapViewReady(view)
    }
}
