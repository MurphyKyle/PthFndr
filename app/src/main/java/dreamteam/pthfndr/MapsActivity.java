package dreamteam.pthfndr;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        LatLng saltLake = new LatLng(40.7608, -111.8910);
        mMap.addMarker(new MarkerOptions().position(saltLake).title("Marker in Salt Lake City"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(saltLake));
    }

    public void signOut(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

    }

    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.d("aaaaaaaa===>", "" + location.getLatitude() + "\n" + location.getLongitude());
        mMap.clear();
        final LatLng loc = new LatLng(location.getLongitude(), location.getLongitude());

        Marker ham = mMap.addMarker(new MarkerOptions().position(loc).title("This is Me"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
    }
}