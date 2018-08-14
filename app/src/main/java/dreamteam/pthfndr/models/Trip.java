package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewDebug;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@IgnoreExtraProperties
public class Trip implements Comparable<Trip>, Parcelable {

    public ArrayList<Path> paths = new ArrayList<>();

    private float averageSpeed;
    private float distance;
    private double time;//in seconds
    private Time timeObj;
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
        averageSpeed = in.readFloat();
        distance = in.readFloat();
        time = in.readDouble();
        maxSpeed = in.readFloat();
        tStart = in.readLong();
    }

    public void end_trip() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - getStart();
        setTimeObj(tDelta);
        setTime(tDelta / 1000.0);
        setAverageSpeed(getAverageSpeed());
        setDistance(getDistance());
    }

    public float getAverageSpeed() {
        float avgSpeed = 0;
        for (Path p : getPaths()) {
            avgSpeed += p.get_speed();
            if (p.get_speed() > getMaxSpeed()) {
                setMaxSpeed(p.get_speed());
            }
        }
        return avgSpeed / getPaths().size();
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }


    public float getDistance() {
        float currentDistance = 0;
        Path currentPath = getPaths().get(0);
        for (int i = 1; i < getPaths().size() - 1; i++) {
            currentDistance += currentPath.getEndLocation().distanceTo(getPaths().get(i).getEndLocation());
        }
        return currentDistance;
    }

    public void setDistance(float distance) {
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
    
    public void setTimeObj(long sysMillis) {
        this.timeObj = new Time(sysMillis);
    }
    
    public void setTimeObj(Time timeObj) {
        this.timeObj = timeObj;
    }
    
    /**
     *
     * @param giveStringValue Specify if you want the string representation
     * @return The time object itself, OR The time object in "hh:mm:ss" format
     * Note: Must cast result to java.sql.Time object or to String
     */
    public Object getTimeObj(boolean giveStringValue) {
        if (giveStringValue) {
            return this.timeObj.toString();
        }
        return this.timeObj;
    }
    
    @Override
    public int compareTo(Trip other) {
        return Comparators.DATE.compare(this, other);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static class Comparators {
        
        public static Comparator<Trip> DATE = (o1, o2) -> o1.date.compareTo(o2.date);

        public static Comparator<Trip> MAXSPEED = (o1, o2) -> Float.compare(o1.maxSpeed, o2.maxSpeed);
    }
}
