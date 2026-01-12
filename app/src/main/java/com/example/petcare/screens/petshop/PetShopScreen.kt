package com.example.petcare.screens.petshop

import android.Manifest
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.petcare.R
import com.example.petcare.components.MapView
import com.example.petcare.viewmodel.PetShopMarker
import com.example.petcare.viewmodel.PetShopViewModel
import com.example.petcare.viewmodel.ViewModelFactory
import com.google.accompanist.permissions.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView as OSMapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PetShopScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: PetShopViewModel = viewModel(factory = ViewModelFactory(context))

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val userLocation by viewModel.userLocation.collectAsState()
    val petShops by viewModel.petShops.collectAsState()

    // Referência estável para a instância do mapa
    var mapViewInstance by remember { mutableStateOf<OSMapView?>(null) }
    var isMapReady by remember { mutableStateOf(false) }

    // Gerencia permissão e atualizações de local
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            viewModel.startLocationUpdates(context)
        }
    }

    // Gerencia o desenho dos marcadores (Sempre que a lista mudar ou o mapa carregar)
    LaunchedEffect(petShops, userLocation, isMapReady) {
        val map = mapViewInstance
        if (map != null && isMapReady) {
            map.overlays.clear()

            // 1. Marcadores de Pet Shops
            val petShopIcon = ContextCompat.getDrawable(context, R.drawable.ic_pets)
            petShops.forEach { petShop ->
                val marker = Marker(map).apply {
                    position = petShop.geoPoint
                    title = petShop.name
                    icon = petShopIcon
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                map.overlays.add(marker)
            }

            // 2. Marcador do Usuário
            userLocation?.let { geoPoint ->
                val userMarker = Marker(map).apply {
                    position = geoPoint
                    title = "Sua Localização"
                    // Estilização simples para o usuário (bolinha azul do sistema se preferir)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                map.overlays.add(userMarker)
            }

            map.invalidate() // Redesenha o mapa
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pet Shops Próximos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (locationPermissionState.status.isGranted) {
                FloatingActionButton(
                    containerColor = Color(0xFF26B6C4),
                    contentColor = Color.White,
                    onClick = {
                        userLocation?.let { loc ->
                            mapViewInstance?.controller?.animateTo(loc)
                            mapViewInstance?.controller?.setZoom(16.0)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location_on),
                        contentDescription = "Minha Localização"
                    )
                }
            }
        },
        bottomBar = {
            if (petShops.isNotEmpty()) {
                // Caixa flutuante para a lista de cards
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .background(Color.Transparent)
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(petShops) { petShop ->
                            PetShopListItem(
                                petShop = petShop,
                                onClick = {
                                    mapViewInstance?.controller?.animateTo(petShop.geoPoint)
                                    mapViewInstance?.controller?.setZoom(18.0)
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (locationPermissionState.status.isGranted) {
                // Componente customizado que criamos para o OSM
                MapView(
                    modifier = Modifier.fillMaxSize(),
                    onMapViewReady = { mapView ->
                        mapViewInstance = mapView
                        mapView.controller.setCenter(viewModel.initialMapPoint)
                        mapView.controller.setZoom(14.0)
                        isMapReady = true
                    }
                )

                if (userLocation == null && petShops.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF26B6C4))
                    }
                }
            } else {
                PermissionRequestUI(locationPermissionState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetShopListItem(
    petShop: PetShopMarker,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(100.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(Color(0xFFF0F9FA), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pets),
                    contentDescription = null,
                    tint = Color(0xFF26B6C4),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = petShop.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1B2D3D)
                )
                Text(
                    text = "Ver no mapa",
                    fontSize = 12.sp,
                    color = Color(0xFF26B6C4)
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestUI(permissionState: PermissionState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location_on),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Precisamos da sua localização para encontrar Pet Shops por perto.",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { permissionState.launchPermissionRequest() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26B6C4))
        ) {
            Text("Conceder Permissão")
        }
    }
}