package dreamteam.pthfndr.models;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Path {
    private Location endLocation;
    private Polyline pl;
    private Color color;
    private int seconds;

    public Path(){

    }

    public Path(Location sl, Location el, Polyline p, Color c, int timePassed){
        setEndLocation(el);
        setPl(p);
        setColor(c);
        setSeconds(timePassed);
    }

    public float get_speed(){
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
