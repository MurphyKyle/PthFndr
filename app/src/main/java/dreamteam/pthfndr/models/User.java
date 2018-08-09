package dreamteam.pthfndr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    private String Name;
    private String UID;
    private ArrayList<Trip> Trips = new ArrayList<>();

    public User(String name, String ID){
        setName(name);
        setUID(ID);
    }

    public User (){

    }

    public void add_trip(Trip t){
        getTrips().add(t);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Trip> getTrips() {
        return Trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        Trips = trips;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}