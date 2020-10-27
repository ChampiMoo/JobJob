package com.res.jobjob.activities.Clientes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.SphericalUtil;
import com.res.jobjob.MainActivity;
import com.res.jobjob.R;
import com.res.jobjob.activities.Socios.MapSocio;
import com.res.jobjob.includes.MyToolbar;
import com.res.jobjob.providers.AuthProvider;
import com.res.jobjob.providers.GeofireProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapCliente extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;

    private Marker mMarker;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private GeofireProvider mGeoFireProvider;

    private LatLng mCurrentLatLng;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private boolean mIsFirstTime = true;

    private List<Marker> mSociosMarkers = new ArrayList<>();




    private AutocompleteSupportFragment mAutocomplete;
    private PlacesClient mPlaces;
    private String mOrigin;
    private LatLng mOriginLatLng;
    private GoogleMap.OnCameraIdleListener mCameraListener;

    private Button btnRequestSocio;


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());



/*                  if (mMarker != null) {
                      mMarker.remove();
                  }

                    mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                            .title("Tu posicion").icon(BitmapDescriptorFactory.fromResource(R.drawable.uusuario)));

*/

                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        getActiveSocios();
                        limitSearch();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cliente);
        MyToolbar.show(this, "Cliente", false);

        btnRequestSocio = findViewById(R.id.btnRequestSocio);

        mAuthProvider = new AuthProvider();

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mGeoFireProvider = new GeofireProvider();

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_api_key));
        }
        mPlaces = Places.createClient(this);
        instanceAutocompleteOrigin();
        onCameraMove();

        btnRequestSocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSocio();
            }
        });




    }

    private void requestSocio() {
        Toast.makeText(MapCliente.this, "Boton ", Toast.LENGTH_SHORT).show();

        if(mOriginLatLng != null){
            Intent intent = new Intent(MapCliente.this,DetailRequest.class);
            intent.putExtra("origen_lat",mOriginLatLng.latitude);
            intent.putExtra("origen_lng",mOriginLatLng.longitude);
            startActivity(intent);

        }
        else{
            Toast.makeText(MapCliente.this, "Debe selecionar el lugar ", Toast.LENGTH_SHORT).show();
        }

    }

    private void limitSearch(){

            LatLng northSide = SphericalUtil.computeOffset(mCurrentLatLng, 50000,0);
            LatLng southSide = SphericalUtil.computeOffset(mCurrentLatLng, 50000,180);
            mAutocomplete.setCountry("MX");
            mAutocomplete.setLocationBias(RectangularBounds.newInstance(southSide,northSide));

    }

    private void onCameraMove(){

        mCameraListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try{

                    Geocoder geocoder = new Geocoder(MapCliente.this);
                    mOriginLatLng = mMap.getCameraPosition().target;
                    List<Address> addressList = geocoder.getFromLocation(mOriginLatLng.latitude, mOriginLatLng.longitude,1);
                    String city = addressList.get(0).getLocality();
                    String country = addressList.get(0).getCountryName();
                    String address = addressList.get(0).getAddressLine(0);
                    mOrigin = address + " "+ city;
                    mAutocomplete.setText(address + " " + city);


                }catch (Exception e){
                    Log.d("Error","Mensaje error : "+ e.getMessage());
                }
            }
        };


    }

    private void instanceAutocompleteOrigin(){

        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);
        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocomplete.setHint("Mi ubicacion");
        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                mOrigin = place.getName();
                mOriginLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

    }

    private void getActiveSocios() {

        mGeoFireProvider.getActiveSocios(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //añadir marcadores de conductores que se conecten

                for (Marker marker : mSociosMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {


                            return;
                        }
                    }
                }

                LatLng socioLatLng = new LatLng(location.latitude, location.longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(socioLatLng).title("Hola Mundo").icon(BitmapDescriptorFactory.fromResource(R.drawable.caja)));
                marker.setTag(key);
                mSociosMarkers.add(marker);
            }

            @Override
            public void onKeyExited(String key) {

                for (Marker marker : mSociosMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            marker.remove();
                            mSociosMarkers.remove(marker);

                            return;
                        }
                    }
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                //actualizar la posicion del socio

                for (Marker marker : mSociosMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {

                            marker.setPosition(new LatLng(location.latitude, location.longitude));

                        }
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
            //

        mMap.setOnCameraIdleListener(mCameraListener);





        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        else  if (requestCode == SETTINGS_REQUEST_CODE && !gpsActived()){
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapCliente.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MapCliente.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.socio_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_logout){

            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout(){
        mAuthProvider.logout();
        Intent intent = new Intent(MapCliente.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}