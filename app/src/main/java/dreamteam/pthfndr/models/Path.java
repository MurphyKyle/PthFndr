package dreamteam.pthfndr.models;

import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Path {
    private MLocation endLocation;
    private MLocation startLocation;
    @Exclude
    private Polyline pl;
    private int color;
    private int seconds;
    public Path() {
    }
    public Path(MLocation sl, MLocation el, Polyline p, int c, int timePassed) {
        setStartLocation(sl);
        setEndLocation(el);
        setPl(p);
        setColor(c);
        setSeconds(timePassed);
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
}
