package dreamteam.pthfndr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Trip {
    public ArrayList<Path> paths = new ArrayList<>();
    private double averageSpeed;
    private double distance;
    private double time;//in seconds
    private Date date;
    private float maxSpeed = 0;
    private long tStart;

    public Trip(){

    }
    public Trip(Date startDate){
        settStart(System.currentTimeMillis());
        setDate(startDate);

    }

    public void end_trip(){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - gettStart();
        setTime(tDelta / 1000.0);
        setAverageSpeed(getAverageSpeed());
        setDistance(getDistance());
    }

    public double getAverageSpeed(){
        double avgSpeed = 0;
        for (Path p: getPaths()) {
            avgSpeed += p.get_speed();
            if (p.get_speed() > getMaxSpeed()){
                setMaxSpeed(p.get_speed());
            }
        }
        return avgSpeed/ getPaths().size();
    }

    public double getDistance(){
        double currentDistance = 0;
        Path currentPath = getPaths().get(0);
        for (int i = 1; i < getPaths().size()-1; i++) {
            currentDistance += currentPath.getEndLocation().distanceTo(getPaths().get(i).getEndLocation());
        }
        return currentDistance;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public long gettStart() {
        return tStart;
    }

    public void settStart(long tStart) {
        this.tStart = tStart;
    }
}
