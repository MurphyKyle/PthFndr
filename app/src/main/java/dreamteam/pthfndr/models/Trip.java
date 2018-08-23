package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@IgnoreExtraProperties
public class Trip implements Comparable<Trip>, Parcelable {
    
    private final int earthRadius = 6371;
    public ArrayList<Path> paths = new ArrayList<>();
    private float averageSpeed = 0.0f;
    private float distance = 0.0f;
    private float time = 0.0f;//in seconds
    private long dateMilis = 0;
    private float maxSpeed = 0.0f;
    private long tStart = 0;

    public Trip() {
        setDateMilis(System.currentTimeMillis());
    }

    public Trip(Date startDate) {
        setStart(System.currentTimeMillis());
        setDateMilis(startDate.getTime());
    }

    private Trip(Parcel in) {
        averageSpeed = in.readFloat();
        distance = in.readFloat();
        time = in.readFloat();
        dateMilis = in.readLong();
        maxSpeed = in.readFloat();
        tStart = in.readLong();
        in.readTypedList(paths, Path.CREATOR);
    }

    public void endTrip() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - getStart();
        setTime((float) (tDelta / 1000.0));
        setAverageSpeed(getAverageSpeed());
        setDistance(getDistance());
    }

    public float getAverageSpeed() {
        float avgSpeed = 0;

        // only calculate the average speed if there are paths saved
        if (paths.size() > 0) {
            for (Path p : getPaths()) {
                avgSpeed += p.getSpeed();
                if (p.getSpeed() > getMaxSpeed()) {
                    setMaxSpeed(p.getSpeed());
                }
            }
            return avgSpeed / getPaths().size();
        }
        // no paths to calculate
        return avgSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public float getDistance() {
        float currentDistance = 0;

        for (Path p : paths) {
            double dLat = degreesToRadians(p.getStartLocation().getLatitude() - p.getEndLocation().getLatitude());
            double dLon = degreesToRadians(p.getStartLocation().getLongitude() - p.getEndLocation().getLongitude());
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(degreesToRadians(p.getStartLocation().getLatitude()))
                    * Math.cos(degreesToRadians(p.getEndLocation().getLatitude())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            currentDistance += earthRadius * c;
        }
        return currentDistance * 0.62137F;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double degreesToRadians(double degree) {
        return degree * (Math.PI / 180);
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public Date getDate() {
        return new Date(dateMilis);
    }

    public void setDateMilis(long milis) {
        this.dateMilis = milis;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public long getStart() {
        return tStart;
    }

    public void setStart(long tStart) {
        this.tStart = tStart;
    }
    
    @Override
    public String toString() {
        String text = "Date:\t" + this.getDate().toString();
        text += "\nTime:\t" + this.getTime();
        text += "\nDistance:\t" + this.getDistance();
        return text;
    }

    
    @Override
    public int compareTo(Trip other) {
        return Comparators.DATE.compare(this, other);
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(averageSpeed);
        parcel.writeFloat(distance);
        parcel.writeFloat(time);
        parcel.writeLong(dateMilis);
        parcel.writeFloat(maxSpeed);
        parcel.writeLong(tStart);
        parcel.writeTypedList(paths);
    }

    public static class Comparators {
        public static Comparator<Trip> DATE = (o1, o2) -> Long.compare(o1.dateMilis, o2.dateMilis);
        
        public static Comparator<Trip> MAXSPEED = (o1, o2) -> Float.compare(o1.maxSpeed, o2.maxSpeed);
    }
    
    public static final Parcelable.Creator<Trip> CREATOR
            = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }
        
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
