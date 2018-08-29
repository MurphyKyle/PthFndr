package dreamteam.pthfndr;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;

import dreamteam.pthfndr.models.MLocation;
import dreamteam.pthfndr.models.MPolyLine;
import dreamteam.pthfndr.models.Path;
import dreamteam.pthfndr.models.Trip;
import dreamteam.pthfndr.models.User;

public class LocServices {
    boolean isActive = false;
    double latitude = 0;
    double longitude = 0;
    Location cLoc;
    Trip trip = new Trip(Calendar.getInstance().getTime());
    long time = 0;
    private GoogleMap mMap;
    private User currentUser;

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (isActive) {

                double longitudeNew = location.getLongitude();
                double latitudeNew = location.getLatitude();

                float currentSpeed = location.getSpeed() * 2.236936F;
                int width = checkPaths(5, longitudeNew, latitudeNew);
                Polyline l = null;
                MPolyLine mp = null;
                if (currentSpeed <= 10) {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 0, 255, 0), width);
                } else if (currentSpeed >= 11 && currentSpeed <= 30) {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 128, 255, 0), width);
                } else if (currentSpeed >= 31 && currentSpeed <= 60) {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 255, 255, 0), width);
                } else if (currentSpeed >= 61 && currentSpeed <= 90) {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 255, 163, 0), width);
                } else if (currentSpeed >= 91) {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 255, 0, 0), width);
                } else {
                    mp = new MPolyLine(longitude, latitude, longitudeNew, latitudeNew, Color.argb(255, 0, 255, 0), width);
                }
                l = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(mp.getStartingLatitude(), mp.getStartingLongitude()), new LatLng(mp.getEndingLatitude(), mp.getEndingLongitude()))
                        .color(mp.getColor())
                );
                trip.paths.add(new Path(new MLocation(cLoc.getSpeed(), latitude, longitude), new MLocation(location.getSpeed(),
                        latitudeNew, longitudeNew), mp, Color.DKGRAY, (int) (System.currentTimeMillis() - time) / 1000));
                cLoc = location;
            }
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        private int checkPaths(int i, double longitudeNew, double latitudeNew) {
            for (Trip t : currentUser.getTrips()) {
                for (Path p : t.getPaths()) {
                    if (p.getEndLocation().getLatitude() >= (latitudeNew - .0004) && p.getEndLocation().getLatitude() <= (latitudeNew + .0004) ||
                            p.getStartLocation().getLatitude() >= (latitudeNew - .0004) && p.getStartLocation().getLatitude() <= (latitudeNew + .0004)) {
                        if (p.getEndLocation().getLongitude() >= (longitudeNew - .0004) && p.getEndLocation().getLongitude() <= (longitudeNew + .0004) ||
                                p.getStartLocation().getLongitude() >= (longitudeNew - .0004) && p.getStartLocation().getLongitude() <= (longitudeNew + .0004)) {
                            i += 5;
                            i = i > 100 ? 100 : i;
                        }
                    }
                }
            }
            return i;
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

    public LocServices(User user, GoogleMap m) {
        currentUser = user;
        mMap = m;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }


    public void NewTrip() {
        trip = new Trip();
    }

}
