package com.res.jobjob.activities.clientes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.database.DatabaseError
import com.google.maps.android.SphericalUtil
import com.res.jobjob.MainActivity
import com.res.jobjob.R
import com.res.jobjob.includes.MyToolbar
import com.res.jobjob.providers.AuthProvider
import com.res.jobjob.providers.GeofireProvider
import java.util.*

class MapCliente : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var  mMarker: Marker
    private lateinit var mMap: GoogleMap
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocation: FusedLocationProviderClient
    private lateinit var mGeoFireProvider: GeofireProvider
    private lateinit var mCurrentLatLng: LatLng
    private var mIsFirstTime = true
    private var mSociosMarkers: MutableList<Marker> = ArrayList()
    private lateinit var mAutocomplete: AutocompleteSupportFragment
    private lateinit var mPlaces: PlacesClient
    private lateinit var mOrigin: String
    private var mOriginLatLng: LatLng? = null
    private lateinit var mCameraListener: OnCameraIdleListener
    private lateinit var btnRequestSocio: Button

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (applicationContext != null) {
                    mCurrentLatLng = LatLng(location.latitude, location.longitude)


/*                  if (mMarker != null) {
                      mMarker.remove();
                  }

                    mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                            .title("Tu posicion").icon(BitmapDescriptorFactory.fromResource(R.drawable.uusuario)));

*/

                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                                    .target(LatLng(location.latitude, location.longitude))
                                    .zoom(15f)
                                    .build()
                    ))
                    if (mIsFirstTime) {
                        mIsFirstTime = false
                        activeSocios
                        limitSearch()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_cliente)
        MyToolbar.show(this, "Cliente", false)
        btnRequestSocio = findViewById(R.id.btnRequestSocio)
        mAuthProvider = AuthProvider()
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mGeoFireProvider = GeofireProvider()
        mMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mMapFragment.getMapAsync(this)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, resources.getString(R.string.google_api_key))
        }
        mPlaces = Places.createClient(this)
        instanceAutocompleteOrigin()
        onCameraMove()
        btnRequestSocio.setOnClickListener{ requestSocio() }
    }

    private fun requestSocio() {
        Toast.makeText(this@MapCliente, "Boton ", Toast.LENGTH_SHORT).show()
        if (mOriginLatLng != null) {
            val intent = Intent(this@MapCliente, DetailRequest::class.java)
            intent.putExtra("origen_lat", mOriginLatLng!!.latitude)
            intent.putExtra("origen_lng", mOriginLatLng!!.longitude)
            startActivity(intent)
        } else {
            Toast.makeText(this@MapCliente, "Debe selecionar el lugar ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limitSearch() {
        val northSide = SphericalUtil.computeOffset(mCurrentLatLng, 50000.0, 0.0)
        val southSide = SphericalUtil.computeOffset(mCurrentLatLng, 50000.0, 180.0)
        mAutocomplete.setCountry("MX")
        mAutocomplete.setLocationBias(RectangularBounds.newInstance(southSide, northSide))
    }

    private fun onCameraMove() {
        mCameraListener = OnCameraIdleListener {
            try {
                val geocoder = Geocoder(this@MapCliente)
                mOriginLatLng = mMap.cameraPosition.target
                val addressList = geocoder.getFromLocation(mOriginLatLng!!.latitude, mOriginLatLng!!.longitude, 1)
                val city = addressList[0].locality
                val country = addressList[0].countryName
                val address = addressList[0].getAddressLine(0)
                mOrigin = "$address $city"
                mAutocomplete.setText("$address $city")
            } catch (e: Exception) {
                Log.d("Error", "Mensaje error : " + e.message)
            }
        }
    }

    private fun instanceAutocompleteOrigin() {
        mAutocomplete = (supportFragmentManager.findFragmentById(R.id.placeAutocompleteOrigin) as AutocompleteSupportFragment?)!!
        mAutocomplete.setPlaceFields(listOf(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))
        mAutocomplete.setHint("Mi ubicacion")
        mAutocomplete.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mOrigin = place.name.toString()
                mOriginLatLng = place.latLng!!
            }

            override fun onError(status: Status) {}
        })
    }//actualizar la posicion del socio

    //añadir marcadores de conductores que se conecten
    private val activeSocios: Unit
        get() {
            mGeoFireProvider.getActiveSocios(mCurrentLatLng).addGeoQueryEventListener(object : GeoQueryEventListener {
                override fun onKeyEntered(key: String, location: GeoLocation) {
                    //añadir marcadores de conductores que se conecten
                    for (marker in mSociosMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                return
                            }
                        }
                    }
                    val socioLatLng = LatLng(location.latitude, location.longitude)
                    val marker = mMap.addMarker(MarkerOptions().position(socioLatLng).title("Hola Mundo").icon(BitmapDescriptorFactory.fromResource(R.drawable.caja)))
                    marker.tag = key
                    mSociosMarkers.add(marker)
                }

                override fun onKeyExited(key: String) {
                    for (marker in mSociosMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                marker.remove()
                                mSociosMarkers.remove(marker)
                                return
                            }
                        }
                    }
                }

                override fun onKeyMoved(key: String, location: GeoLocation) {

                    //actualizar la posicion del socio
                    for (marker in mSociosMarkers) {
                        if (marker.tag != null) {
                            if (marker.tag == key) {
                                marker.position = LatLng(location.latitude, location.longitude)
                            }
                        }
                    }
                }

                override fun onGeoQueryReady() {}
                override fun onGeoQueryError(error: DatabaseError) {}
            })
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        //
        mMap.setOnCameraIdleListener(mCameraListener)
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.smallestDisplacement = 5f
        startLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                        mMap.isMyLocationEnabled = true
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
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap.isMyLocationEnabled = true
        } else if (requestCode == SETTINGS_REQUEST_CODE && !gpsActived()) {
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

    private fun startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                    mMap.isMyLocationEnabled = true
                } else {
                    showAlertDialogNOGPS()
                }
            } else {
                checkLocationPermissions()
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mMap.isMyLocationEnabled = true
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
                        .setPositiveButton("OK") { _, _ -> ActivityCompat.requestPermissions(this@MapCliente, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE) }
                        .create()
                        .show()
            } else {
                ActivityCompat.requestPermissions(this@MapCliente, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
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
        mAuthProvider.logout()
        val intent = Intent(this@MapCliente, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private const val SETTINGS_REQUEST_CODE = 2
    }
}