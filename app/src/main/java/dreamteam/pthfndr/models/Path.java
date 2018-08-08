package dreamteam.pthfndr.models;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Path {
    Location endLocation;
    Polyline pl;
    Color color;
    int seconds;

    public Path(Location sl, Location el, Polyline p, Color c, int timePassed){
        endLocation = el;
        pl = p;
        color = c;
        seconds = timePassed;
    }

    public float get_speed(){
        return endLocation.getSpeed() * 3.6F;
    }

}
