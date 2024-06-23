package com.ceancof.deliverytracker

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ceancof.deliverytracker.Ubicacion.Ubicaciones
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var ubicacion: FusedLocationProviderClient
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 100 // Define el códigode solicitud

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance() // Inicializa FirebaseDatabase
        myRef = database.getReference("ubicacion")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        obtenerUbicacion()
    }



    private fun obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            ubicacion = LocationServices.getFusedLocationProviderClient(this)
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val ubicacionData = Ubicaciones(latitude, longitude)
                    val llave: String? = database.getReference().push().getKey()
                    if (llave != null) {
                        myRef.child(llave).setValue(ubicacionData)
                            .addOnSuccessListener {
                                Log.d("Firebase", "Location data saved successfully!")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firebase", "Error saving location data: ${e.message}")
                            }
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    fun obtenerCamino(view: View) {
        obtenerUbicacion()
    }
}