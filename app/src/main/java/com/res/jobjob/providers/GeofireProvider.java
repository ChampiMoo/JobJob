package com.res.jobjob.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDatabase;
    private GeoFire mGeoFire;

    public GeofireProvider(){

        mDatabase = FirebaseDatabase.getInstance().getReference().child("active_socios");
        mGeoFire = new GeoFire(mDatabase);


    }

    public void saveLocation(String idSocio, LatLng latLng){
        mGeoFire.setLocation(idSocio, new GeoLocation(latLng.latitude, latLng.longitude));
    }


    public void removeLocation(String idSocio){

        mGeoFire.removeLocation(idSocio);
    }

    public GeoQuery getActiveSocios(LatLng latLng){
        GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),5);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

}
