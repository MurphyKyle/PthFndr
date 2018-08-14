package dreamteam.pthfndr.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Path implements Parcelable {
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
    private Polyline pl;
    private int color;
    private int seconds;
    private MLocation endLocation;
    private MLocation startLocation;

    public Path() {
    }

    public Path(MLocation sl, MLocation el, Polyline p, int c, int timePassed) {
        setStartLocation(sl);
        setEndLocation(el);
        setPl(p);
        setColor(c);
        setSeconds(timePassed);
    }

    protected Path(Parcel in) {
        endLocation = (MLocation) in.readValue(getClass().getClassLoader());
        startLocation = (MLocation) in.readValue(getClass().getClassLoader());
        pl = (Polyline) in.readValue(getClass().getClassLoader());
        color = in.readInt();
        seconds = in.readInt();
    }

    public MLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(MLocation startLocation) {
        this.startLocation = startLocation;
    }

    public float get_speed() {
        return getEndLocation().getSpeed() * 3.6F;
    }

    public MLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(MLocation endLocation) {
        this.endLocation = endLocation;
    }

    @Exclude
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