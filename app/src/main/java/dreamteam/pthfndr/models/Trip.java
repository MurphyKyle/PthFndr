package dreamteam.pthfndr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Trip {
    @Exclude
    public ArrayList<Path> paths = new ArrayList<>();
    double averageSpeed;
    double distance;
    double time;//in seconds
    Date date;
    float maxSpeed = 0;
    String userID;
    long tStart;

    public Trip(Date startDate, String userID){
        this.userID = userID;
        tStart = System.currentTimeMillis();
        date = startDate;
    }

    public void end_trip(){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        time = tDelta / 1000.0;
        averageSpeed = getAverageSpeed();
        distance = getDistance();
    }

    public double getAverageSpeed(){
        double avgSpeed = 0;
        for (Path p: paths) {
            avgSpeed += p.get_speed();
            if (p.get_speed() > maxSpeed){
                maxSpeed = p.get_speed();
            }
        }
        return avgSpeed/paths.size();
    }

    public double getDistance(){
        double currentDistance = 0;
        Path currentPath = paths.get(0);
        for (int i = 1; i < paths.size()-1; i++) {
            currentDistance += currentPath.endLocation.distanceTo(paths.get(i).endLocation);
        }
        return currentDistance;
    }

}
