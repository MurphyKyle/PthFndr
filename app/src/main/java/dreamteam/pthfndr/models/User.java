package dreamteam.pthfndr.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    private String Name;
    @Exclude
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
    @Exclude
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Trips", Trips);
        return result;
    }
}