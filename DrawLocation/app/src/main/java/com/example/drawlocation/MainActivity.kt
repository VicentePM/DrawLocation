package com.example.drawlocation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.drawlocation.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var mapa: GoogleMap? = null
    private var marker: Marker? = null
    private var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Localizacion
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        locationConfig()
        requestLocationPermissions()

        //Mapas
        val fragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragment.getMapAsync(this)

    }

    /* GET LOCATION */


    private fun locationConfig() {
        locationRequest = LocationRequest.Builder(500).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setMinUpdateDistanceMeters(2F)
        }.build()

        checkConfig(locationRequest)
    }

    private fun checkConfig(locationRequest: LocationRequest) {
        val locationSettingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(locationSettingsBuilder.build())
        task.addOnSuccessListener {
            getLocation()
        }
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            p0.lastLocation?.let {
                printLocation(it)

                if (mapa != null) {

                    //Centrar camara
                    mapa!!.setOnMapLoadedCallback {
                    mapa!!.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude), 20f),
                    )
                }

                    if (marker != null) {
                        var rectOptions = PolylineOptions()
                            .color(Color.CYAN)
                            .width(10f)
                            .add(marker?.position)
                            .add(LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude))
                        mapa!!.addPolyline(rectOptions)
                        marker?.remove()
                    }
//                     añadir marker
                    val options1 = MarkerOptions()
                        .position(LatLng(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude))
                        .title("Tu localizacion")


                    marker = mapa!!.addMarker(options1)
                    //Linea

//                    polyline?.let {
//                        val points = it.points
//                        points.add(marker?.position)
//                        it.points = points
//                    }
//
//                    val polylineOptions = PolylineOptions()
//                        .color(Color.BLACK)
//                        .width(10f)
//                    polyline = mapa!!.addPolyline(polylineOptions)



                }

            }
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun printLocation(location: Location) {
                binding.textView.text = """
            Latitud: ${location.latitude}, Longitud: ${location.longitude}
            Altitud: ${location.altitude}
            Accuracy: ${location.accuracy}
            Proveedor: ${location.provider}
            Orientación: ${location.bearing}
            """
    }

    /* REQUEST PERMISSIONS */
    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showAlertDialog()
        } else {
            somePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val somePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (!it.containsValue(false)) {
            getLocation()
        } else {
            Toast.makeText(
                this,
                "No se han aceptado los permisos necesarios",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Los permisos de ubicación son necesarios para mostrar el tiempo")
        builder.setNegativeButton("Rechazar", null)
        builder.setPositiveButton("Aceptar") { _, _ ->
            somePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        builder.create().show()
    }

    override fun onStop() {
        super.onStop()
        locationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (this::locationRequest.isInitialized) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    /* MAPS */

    override fun onMapReady(map: GoogleMap) {
        mapa = map
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        val uiSettings = map.uiSettings
        uiSettings.isZoomControlsEnabled = true //controles de zoom

        uiSettings.isCompassEnabled = true //mostrar la brújula

        uiSettings.isZoomGesturesEnabled = true //gestos de zoom

        uiSettings.isScrollGesturesEnabled = true //Gestos de scroll

        uiSettings.isTiltGesturesEnabled = true //Gestos de ángulo

        uiSettings.isRotateGesturesEnabled = true //Gestos de rotación

        map.setMinZoomPreference(2.0f)

    }
}