package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MLocation implements Parcelable {
   
    private double latitude = 1;
    private double longitude = 1;
    private float speed = 1;

    public MLocation() {
    }

    public MLocation(float s, double lat, double lng) {
        latitude = lat;
        longitude = lng;
        speed = s;
    }

    private MLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        speed = in.readFloat();
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

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeFloat(speed);
    }
    
    public static final Parcelable.Creator<MLocation> CREATOR
            = new Parcelable.Creator<MLocation>() {
        public MLocation createFromParcel(Parcel in) {
            return new MLocation(in);
        }
        
        public MLocation[] newArray(int size) {
            return new MLocation[size];
        }
    };
}