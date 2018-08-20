package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Parcelable {

    @Exclude
    public static final String DEFAULT_NAME = "no name";
    @Exclude
    private String UID = "no uid";
    private String Name = DEFAULT_NAME;
    private ArrayList<Trip> Trips = new ArrayList<>();

    public User() { }

    public User(String name, String ID) {
        setName(name);
        setUID(ID);
    }

    private User(Parcel in) {
        UID = in.readString();
        Name = in.readString();
        in.readTypedList(Trips, Trip.CREATOR);
    }

    public void addTrip(Trip t) {
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
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Trips", Trips);
        return result;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(UID);
        parcel.writeString(Name);
        parcel.writeTypedList(Trips);
    }
    
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}