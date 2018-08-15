package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Contacts;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User  implements Parcelable{

    private int mData;

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


    private String Name = "no name";
    @Exclude
    private String UID = "no uid";
    private ArrayList<Trip> Trips = new ArrayList<>();

    public User(String name, String ID){
        setName(name);
        setUID(ID);
    }
    public User (){

    }
    private User(Parcel in) {
        mData = in.readInt();
        UID = in.readString();
        Name = in.readString();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mData);
        parcel.writeString(UID);
        parcel.writeString(Name);
        parcel.writeList(Trips);

    }
}