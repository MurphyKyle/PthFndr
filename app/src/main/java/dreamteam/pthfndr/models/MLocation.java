package dreamteam.pthfndr.models;

public class MLocation {
    private double latitude;
    private double longitude;
    private float speed;
    public MLocation() {

    }
    public MLocation(float s, double lat, double lng) {
        latitude = lat;
        longitude = lng;
        speed = s;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}