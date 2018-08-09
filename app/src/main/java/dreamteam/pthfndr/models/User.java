package dreamteam.pthfndr.models;

import java.util.ArrayList;

public class User {

    private String Name;
    private ArrayList<Trip> Trips = new ArrayList<>();

    public User(String name){
        setName(name);
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

    public ArrayList<Trip> getTrips() {
        return Trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        Trips = trips;
    }
}
