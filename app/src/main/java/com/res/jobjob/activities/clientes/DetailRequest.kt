package com.res.jobjob.activities.clientes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.res.jobjob.R
import com.res.jobjob.includes.MyToolbar

class DetailRequest : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mExtraOriginLat = 0.0
    private var mExtraOriginLng = 0.0
    private var mOriginLatLng: LatLng? = null
    private lateinit var btnRequestNow: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_request)
        MyToolbar.show(this, "Tus datos", true)
        mMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment!!.getMapAsync(this)
        btnRequestNow = findViewById(R.id.btnRequestNow)
        mExtraOriginLat = intent.getDoubleExtra("origen_lat", 0.0)
        mExtraOriginLng = intent.getDoubleExtra("origen_lng", 0.0)
        mOriginLatLng = LatLng(mExtraOriginLat, mExtraOriginLng)
        btnRequestNow.setOnClickListener {
            Toast.makeText(this@DetailRequest, "Hasta aqui todo bien", Toast.LENGTH_SHORT).show()
            goToRequestSocio()
        }
    }

    private fun goToRequestSocio() {
        val i = Intent(this@DetailRequest, RequestSocio::class.java)
        Toast.makeText(this, "antes de iniciar la actividad de esperar", Toast.LENGTH_SHORT).show()
        startActivity(i)
        finish()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        //
        mMap!!.addMarker(MarkerOptions().position(mOriginLatLng!!).title("Mi casa ").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)))
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(mOriginLatLng).zoom(14f).build()))
    }
}