package dreamteam.pthfndr.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MPolyLine implements Parcelable {

    private double startingLongitude;
    private double startingLatitude;
    private double endingLongitude;
    private double endingLatitude;
    private int color;
    private int width = 5;

    public MPolyLine() {
    }

    public MPolyLine(double startLong, double startLat, double endLong, double endLat, int c, int w) {
        startingLongitude = startLong;
        startingLatitude = startLat;
        endingLongitude = endLong;
        endingLatitude = endLat;
        color = c;
        width = w;
    }

    private MPolyLine(Parcel in) {
        startingLongitude = in.readDouble();
        startingLatitude = in.readDouble();
        endingLongitude = in.readDouble();
        endingLatitude = in.readDouble();
        color = in.readInt();
        width = in.readInt();;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(startingLongitude);
        dest.writeDouble(startingLatitude);
        dest.writeDouble(endingLongitude);
        dest.writeDouble(endingLatitude);
        dest.writeInt(color);
        dest.writeInt(width);
    }

    public static final Parcelable.Creator<MPolyLine> CREATOR
            = new Parcelable.Creator<MPolyLine>() {
        public MPolyLine createFromParcel(Parcel in) {
            return new MPolyLine(in);
        }

        public MPolyLine[] newArray(int size) {
            return new MPolyLine[size];
        }
    };

    public double getStartingLongitude() {
        return startingLongitude;
    }

    public void setStartingLongitude(double startingLongitude) {
        this.startingLongitude = startingLongitude;
    }

    public double getStartingLatitude() {
        return startingLatitude;
    }

    public void setStartingLatitude(double startingLatitude) {
        this.startingLatitude = startingLatitude;
    }

    public double getEndingLongitude() {
        return endingLongitude;
    }

    public void setEndingLongitude(double endingLongitude) {
        this.endingLongitude = endingLongitude;
    }

    public double getEndingLatitude() {
        return endingLatitude;
    }

    public void setEndingLatitude(double endingLatitude) {
        this.endingLatitude = endingLatitude;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
