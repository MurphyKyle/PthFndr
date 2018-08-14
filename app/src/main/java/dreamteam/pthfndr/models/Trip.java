package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewDebug;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Trip implements Parcelable{

    public ArrayList<Path> paths = new ArrayList<>();
    private double averageSpeed;
    private double distance;
    private double time;//in seconds
    private Date date;
    private float maxSpeed = 0;
    private long tStart;

    public static final Parcelable.Creator<Trip> CREATOR
            = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public Trip() {
    }

    public Trip(Date startDate) {
        setStart(System.currentTimeMillis());
        setDate(startDate);
    }

    public Trip(Parcel in){
        averageSpeed = in.readDouble();
        distance = in.readDouble();
        time = in.readDouble();
        maxSpeed = in.readFloat();
        tStart = in.readLong();
    }

    public void end_trip() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - getStart();
        setTime(tDelta / 1000.0);
        setAverageSpeed(getAverageSpeed());
        setDistance(getDistance());
    }

    public double getAverageSpeed() {
        if(averageSpeed!=0  ){
            return averageSpeed;
        }
        double avgSpeed = 0;
        for (Path p : getPaths()) {
            avgSpeed += p.get_speed();
            if (p.get_speed() > getMaxSpeed()) {
                setMaxSpeed(p.get_speed());
            }
        }
        return avgSpeed / getPaths().size();
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getDistance() {
        if(distance != 0){
            return distance;
        }
        double currentDistance = 0;
        Path currentPath = getPaths().get(0);
        for (int i = 1; i < getPaths().size() - 1; i++) {
            currentDistance += currentPath.getEndLocation().distanceTo(getPaths().get(i).getEndLocation());
        }
        return currentDistance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.
        parcel.writeValue(date);

    }
}
