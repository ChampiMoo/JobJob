package com.res.jobjob.providers

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GeofireProvider {

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("active_socios")
    private val mGeoFire: GeoFire

    fun saveLocation(idSocio: String?, latLng: LatLng) {
        mGeoFire.setLocation(idSocio, GeoLocation(latLng.latitude, latLng.longitude))
    }

    fun removeLocation(idSocio: String?) {
        mGeoFire.removeLocation(idSocio)
    }

    fun getActiveSocios(latLng: LatLng?): GeoQuery {
        val geoQuery = mGeoFire.queryAtLocation(GeoLocation(latLng!!.latitude, latLng.longitude), 5.0)
        geoQuery.removeAllListeners()
        return geoQuery
    }

    init {
        mGeoFire = GeoFire(mDatabase)
    }
}