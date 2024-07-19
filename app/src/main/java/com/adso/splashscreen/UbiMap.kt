package com.adso.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import com.adso.splashscreen.Anuncios
import com.adso.splashscreen.ContenedorItemCuenta
import com.adso.splashscreen.InvitarAmigo
import com.adso.splashscreen.R
import com.adso.splashscreen.SignInActivity
import com.adso.splashscreen.databinding.ActivityUbiMapBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UbiMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityUbiMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationPermissionGranted = false
    private lateinit var placesClient: PlacesClient
    private lateinit var geoApiContext: GeoApiContext
    private lateinit var destinationAutoComplete: AutoCompleteTextView
    private lateinit var btnContinue: Button
    private var currentLocation: LatLng? = null
    private var destinationLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Log.d("UbiMapFlow", "Iniciando onCreate de UbiMap")
            binding = ActivityUbiMapBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Inicializar Places
            if (!Places.isInitialized()) {
                Places.initialize(applicationContext, "AIzaSyCx-HzI6A3sV31e7P35h0jXbS8kFwl1f10")
            }

            // Inicializar GeoApiContext
            geoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build()

            // Setup Toolbar
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            // Setup DrawerLayout and NavigationView
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navigationView: NavigationView = findViewById(R.id.nav_view)
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_account -> {
                        val intent = Intent(this, ContenedorItemCuenta::class.java)
                        startActivity(intent)
                    }
                    R.id.nav_invite -> {
                        val intent = Intent(this, InvitarAmigo::class.java)
                        startActivity(intent)
                    }
                    R.id.nav_ads -> {
                        val intent = Intent(this, Anuncios::class.java)
                        startActivity(intent)
                    }
                    R.id.sesion -> {
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    }
                }
                drawerLayout.closeDrawers()
                true
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)

            destinationAutoComplete = findViewById(R.id.destinationAutoComplete)
            btnContinue = findViewById(R.id.btnContinue)
            setupAutoComplete()

            btnContinue.setOnClickListener {
                // Aquí puedes agregar la lógica para continuar con el siguiente paso
                Toast.makeText(this, "Continuando con la ruta...", Toast.LENGTH_SHORT).show()
            }

            Log.d("UbiMapFlow", "onCreate de UbiMap completado")
        } catch (e: Exception) {
            Log.e("UbiMapFlow", "Error en onCreate de UbiMap", e)
            Toast.makeText(this, "Error al cargar el mapa: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupAutoComplete() {
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("UbiMapFlow", "Place: ${place.name}, ${place.id}")
                place.latLng?.let { latLng ->
                    destinationLocation = latLng
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(latLng).title(place.name))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    currentLocation?.let { origin ->
                        drawRoute(origin, latLng)
                    }
                }
            }

            override fun onError(status: Status) {
                val errorMessage = "An error occurred: ${status.statusMessage} (code: ${status.statusCode})"
                Log.e("UbiMapFlow", errorMessage)
                Toast.makeText(this@UbiMap, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun performAutoComplete(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions.map { it.getFullText(null).toString() }
            val adapter = destinationAutoComplete.adapter as ArrayAdapter<String>
            adapter.clear()
            adapter.addAll(predictions)
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("UbiMapFlow", "Error en autocompletado", exception)
        }
    }

    private fun searchLocation(prediction: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(prediction)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val placeId = response.autocompletePredictions.firstOrNull()?.placeId
            if (placeId != null) {
                val placeFields = listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
                val placeRequest = com.google.android.libraries.places.api.net.FetchPlaceRequest.newInstance(placeId, placeFields)

                placesClient.fetchPlace(placeRequest).addOnSuccessListener { placeResponse ->
                    val place = placeResponse.place
                    place.latLng?.let { latLng ->
                        destinationLocation = latLng
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(latLng).title(prediction))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        currentLocation?.let { origin ->
                            drawRoute(origin, latLng)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.e("UbiMapFlow", "Error al buscar la ubicación", exception)
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("UbiMapFlow", "Error al obtener el placeId", exception)
        }
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = DirectionsApi.newRequest(geoApiContext)
                    .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                    .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                    .await()

                withContext(Dispatchers.Main) {
                    addPolyline(result)
                    btnContinue.isEnabled = true
                }
            } catch (e: Exception) {
                Log.e("UbiMapFlow", "Error al trazar la ruta", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UbiMap, "Error al trazar la ruta: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addPolyline(result: com.google.maps.model.DirectionsResult) {
        val decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.encodedPath)
        mMap.addPolyline(PolylineOptions().addAll(decodedPath))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            Log.d("UbiMapFlow", "Mapa listo")
            mMap = googleMap
            getLocationPermission()
            updateLocationUI()
            getDeviceLocation()
        } catch (e: Exception) {
            Log.e("UbiMapFlow", "Error al configurar el mapa", e)
            Toast.makeText(this, "Error al configurar el mapa: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("UbiMapFlow", "Error: ${e.message}", e)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            currentLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d("UbiMapFlow", "Current location is null. Using defaults.")
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
        private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    }
}






