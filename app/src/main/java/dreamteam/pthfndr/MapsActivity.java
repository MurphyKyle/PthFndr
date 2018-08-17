package dreamteam.pthfndr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dreamteam.pthfndr.models.MLocation;
import dreamteam.pthfndr.models.Path;
import dreamteam.pthfndr.models.Trip;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static MapsActivity thisRef;
    boolean isActive = false;
    dreamteam.pthfndr.models.User currentUser;
    double latitude = 0;
    double longitude = 0;
    Location cLoc;
    Trip trip = new Trip(Calendar.getInstance().getTime());
    long time = 0;
    private GoogleMap mMap;
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (isActive) {
                double longitudeNew = location.getLongitude();
                double latitudeNew = location.getLatitude();
                Polyline l = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(latitude, longitude), new LatLng(latitudeNew, longitudeNew))
                        .width(5)
                        .color(Color.BLUE)
                );
                trip.paths.add(new Path(new MLocation(cLoc.getSpeed(), latitude, longitude), new MLocation(location.getSpeed(), latitudeNew, longitudeNew), l, Color.DKGRAY, (int) (System.currentTimeMillis() - time) / 1000));
                cLoc = location;
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
    private boolean mLocationPermissionGranted;
    private FirebaseUser authUser;
    private DatabaseReference fDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] keys = getIntent().getExtras().keySet().toArray(new String[1]);
        currentUser = getIntent().getExtras().getParcelable(keys[0]);
        super.onCreate(savedInstanceState);
        authUser = FirebaseAuth.getInstance().getCurrentUser();
        fDB = FirebaseDatabase.getInstance().getReference().child("users");
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        thisRef = this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (currentUser != null) {
            for (Trip t : currentUser.getTrips()) {
                for (Path p : t.getPaths()) {
                    Polyline l = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(p.getEndLocation().getLatitude(), p.getEndLocation().getLongitude()), new LatLng(p.getStartLocation().getLatitude(), p.getStartLocation().getLongitude()))
                            .width(5)
                            .color(Color.DKGRAY)
                    );
                }
            }
        }

        getLocationPermission();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setBuildingsEnabled(true);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        cLoc = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

        time = System.currentTimeMillis();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
            }
        }
    }

    public void signOut(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void manageTrip(View view) {
        isActive = !isActive;

        Button b = findViewById(R.id.TripButton);
        if (isActive) {
            if (trip == null) {
                trip = new Trip();
            }
            b.setText(R.string.endTrip);
        } else {
            mMap.clear();
            for (Trip t : currentUser.getTrips()) {
                for (Path p : t.getPaths()) {
                    Polyline l = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(p.getEndLocation().getLatitude(), p.getEndLocation().getLongitude()), new LatLng(p.getStartLocation().getLatitude(), p.getStartLocation().getLongitude()))
                            .width(5)
                            .color(Color.DKGRAY)
                    );
                }
            }
            trip.endTrip();
            currentUser.addTrip(trip);
            trip = new Trip();
            b.setText(R.string.startTrip);
            updateUser();
        }
    }

    private void updateUser() {
        fDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> userValues = currentUser.toMap();
                Map<String, Object> userUpdates = new HashMap<>();
                userUpdates.put(authUser.getUid(), userValues);
                fDB.updateChildren(userUpdates, (databaseError, databaseReference) ->
                        Toast.makeText(thisRef, "Trip Saved!", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}