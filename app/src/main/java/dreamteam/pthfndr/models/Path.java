package dreamteam.pthfndr.models;

import android.graphics.Color;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Path implements Parcelable {
    private Location endLocation;
    private Polyline pl;
    private int color;
    private int seconds;

    public Path(){
    }

    public Path(Location el, Polyline p, int c, int timePassed){
        setEndLocation(el);
        setPl(p);
        setColor(c);
        setSeconds(timePassed);
    }

    protected Path(Parcel in) {
        endLocation = (Location)in.readValue(getClass().getClassLoader());
        pl = (Polyline) in.readValue(getClass().getClassLoader());
        color = in.readInt();
        seconds = in.readInt();
    }

    public static final Creator<Path> CREATOR = new Creator<Path>() {
        @Override
        public Path createFromParcel(Parcel in) {
            return new Path(in);
        }

        @Override
        public Path[] newArray(int size) {
            return new Path[size];
        }
    };

    public float get_speed() {
        return getEndLocation().getSpeed() * 3.6F;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public Polyline getPl() {
        return pl;
    }

    public void setPl(Polyline pl) {
        this.pl = pl;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(color);
        parcel.writeInt(seconds);
        parcel.writeValue(endLocation);
        parcel.writeValue(pl);
    }
}
