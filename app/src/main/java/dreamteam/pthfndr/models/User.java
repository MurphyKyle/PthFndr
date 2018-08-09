package dreamteam.pthfndr.models;

import java.util.ArrayList;

public class User {

    private String Name;
    private String UID;
    private ArrayList<Trip> Trips = new ArrayList<>();

    public User(String name, String ID){
        setName(name);
        setUID(ID);
    }

    public void add_trip(Trip t){
        getTrips().add(t);
    }

    public ArrayList<Trip> get_all_trips(){
        return getTrips();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public ArrayList<Trip> getTrips() {
        return Trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        Trips = trips;
    }
}
