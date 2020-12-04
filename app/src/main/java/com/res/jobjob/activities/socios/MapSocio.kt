package com.res.jobjob.activities.socios

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.res.jobjob.MainActivity
import com.res.jobjob.R
import com.res.jobjob.includes.MyToolbar
import com.res.jobjob.providers.AuthProvider
import com.res.jobjob.providers.GeofireProvider

class MapSocio : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mAuthProvider: AuthProvider? = null
    private var mGeofireProvider: GeofireProvider? = null
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocation: FusedLocationProviderClient? = null
    private var mMarker: Marker? = null
    private lateinit var btnConnect: Button
    private var isConnect = false
    private var mCurrentLatLng: LatLng? = null

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (applicationContext != null) {
                    mCurrentLatLng = LatLng(location.latitude, location.longitude)
                    if (mMarker != null) {
                        mMarker!!.remove()
                    }
                    mMarker = mMap!!.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude))
                            .title("Tu posicion").icon(BitmapDescriptorFactory.fromResource(R.drawable.caja)))
                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                    .target(LatLng(location.latitude, location.longitude))
                                    .zoom(17f)
                                    .build()
                    ))
                    updateLocation()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_socio)
        MyToolbar.show(this, "Socio", false)
        mAuthProvider = AuthProvider()
        mGeofireProvider = GeofireProvider()
        btnConnect = findViewById(R.id.btnConnect)
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment!!.getMapAsync(this)
        btnConnect.setOnClickListener{
            if (isConnect) {
                disconect()
            } else {
                startLocation()
            }
        }
    }

    fun updateLocation() {
        if (mAuthProvider!!.existSession() && mCurrentLatLng != null) {
            mGeofireProvider!!.saveLocation(mAuthProvider!!.id, mCurrentLatLng!!)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = 5f
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                        mMap!!.isMyLocationEnabled = true
                    } else {
                        showAlertDialogNOGPS()
                    }
                } else {
                    checkLocationPermissions()
                }
            } else {
                checkLocationPermissions()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mFusedLocation!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap!!.isMyLocationEnabled = true
        } else {
            showAlertDialogNOGPS()
        }
    }

    private fun showAlertDialogNOGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones") { _, _ -> startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE) }.create().show()
    }

    private fun gpsActived(): Boolean {
        var isActive = false
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true
        }
        return isActive
    }

    private fun disconect() {
        if (mFusedLocation != null) {
            btnConnect.text = "Conectarse"
            isConnect = false
            mFusedLocation!!.removeLocationUpdates(mLocationCallback)
            if (mAuthProvider!!.existSession()) {
                mGeofireProvider!!.removeLocation(mAuthProvider!!.id)
            }
        } else {
            Toast.makeText(this@MapSocio, "No te puedes desconectar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    btnConnect.text = "Desconectarse"
                    isConnect = true
                    mFusedLocation!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                    mMap!!.isMyLocationEnabled = true
                } else {
                    showAlertDialogNOGPS()
                }
            } else {
                checkLocationPermissions()
            }
        } else {
            if (gpsActived()) {
                mFusedLocation!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mMap!!.isMyLocationEnabled = true
            } else {
                showAlertDialogNOGPS()
            }
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK") { _, _ -> ActivityCompat.requestPermissions(this@MapSocio, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE) }
                        .create()
                        .show()
            } else {
                ActivityCompat.requestPermissions(this@MapSocio, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.socio_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        disconect()
        mAuthProvider!!.logout()
        val intent = Intent(this@MapSocio, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val SETTINGS_REQUEST_CODE = 2
    }
}