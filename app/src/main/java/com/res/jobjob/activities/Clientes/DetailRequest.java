package com.res.jobjob.activities.Clientes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.res.jobjob.R;
import com.res.jobjob.includes.MyToolbar;

public class DetailRequest extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private double mExtraOriginLat;
    private double mExtraOriginLng;

    private LatLng mOriginLatLng;

    private Button btnRequestNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);

        MyToolbar.show(this,"Tus datos",true);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        btnRequestNow = findViewById(R.id.btnRequestNow);

        mExtraOriginLat = getIntent().getDoubleExtra("origen_lat",0);
        mExtraOriginLng = getIntent().getDoubleExtra("origen_lng",0);

        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);

        btnRequestNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailRequest.this, "Hasta aqui todo bien", Toast.LENGTH_SHORT).show();
                goToRequestSocio();
            }
        });
    }

    private void goToRequestSocio() {

        Intent i = new Intent(DetailRequest.this, RequestSocio.class );
        Toast.makeText(this, "antes de iniciar la actividad de esperar", Toast.LENGTH_SHORT).show();
        startActivity(i);
        finish();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //

        mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Mi casa ").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(mOriginLatLng).zoom(14f).build()));
    }
}