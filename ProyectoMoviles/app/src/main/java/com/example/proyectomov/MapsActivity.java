package com.example.proyectomov;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
 //Define Toolbar
        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_action_name); //Define icono para toolbar

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.top_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextAct;
            switch (item.getItemId()) {
                case R.id.perfil:
                    //Toast.makeText ( this, "Perfil!", Toast.LENGTH_LONG).show ();
                    nextAct= new Intent(this,PerfilActivity.class);
                    startActivity(nextAct);
                    return true;

                case R.id.my_list:
                    nextAct= new Intent(this,MyListActivity.class);
                    startActivity(nextAct);
                    return true;
                case R.id.my_cont:
                    nextAct= new Intent(this,MyContActivity.class);
                    startActivity(nextAct);
                    return true;

                case R.id.recibidas:
                    nextAct= new Intent(this,RecepcionesActivity.class);
                    startActivity(nextAct);
                    return true;

                case R.id.map:
                    nextAct= new Intent(this,MapaListActivity.class);
                    startActivity(nextAct);
                    return true;

                case R.id.config:
                    nextAct= new Intent(this,ConfigActivity.class);
                    startActivity(nextAct);
                    return true;
                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        miUbicacion();
    }

    private void agregarMarcador(double lat, double lng)
    {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                        .position(coordenadas)
                        .title("Mi ubicacion actual")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ubicacion))
        );
        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location)
    {
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

    LocationListener loclistener = new LocationListener()
    {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onProviderDisabled(@NonNull String provider) {}
    };

    private void miUbicacion()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,loclistener);
    }
}