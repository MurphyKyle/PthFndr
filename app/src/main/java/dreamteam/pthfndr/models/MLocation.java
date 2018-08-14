package dreamteam.pthfndr.models;

import com.google.android.gms.maps.model.LatLng;

public class MLocation {
    private LatLng place;
    private float speed;

    public MLocation() {

    }

    public MLocation(float s, LatLng p) {
        place = p;
        speed = s;
    }

    public LatLng getPlace() {
        return place;
    }

    public void setPlace(LatLng place) {
        this.place = place;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}