package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import dreamteam.pthfndr.models.Trip;
import dreamteam.pthfndr.models.User;

/**
 * Lists the user's trips and aggregates the selected trip's info to the view
 */
public class TripHistoryActivity extends AppCompatActivity {
	
	private User theUser;
	private TextView txtDate;
	private TextView txtTime;
	private TextView txtDistance;
	private TextView txtMaxSpeed;
	private TextView txtAverageSpeed;
	private ListView tripListView;
	
	private ArrayList<Trip> activeTrips = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// set the view
		setContentView(R.layout.activity_trip_history);
		// set the view children
		setViewObjects();
		
		// ignoring what the parcel is actually called..
		if (getIntent().getExtras() != null) {
			
			String[] keys = getIntent().getExtras().keySet().toArray(new String[1]);
			// ..get the user - we should only get the user's data into the activity, nothing else
			setTheUser( getIntent().getExtras().getParcelable(keys[0]) );
			ArrayList<Trip> trips = getTheUser().getTrips();
			
			if (trips.size() > 0) {
				// build the list of selectable trips
				buildTripList(trips);
			} else {
				// notify if the user has no trips to view
				setEmptyTripList();
			}
		}
	}
	
	private void setViewObjects() {
		txtDate = findViewById(R.id.txtDate);
		txtTime = findViewById(R.id.txtTime);
		txtDistance = findViewById(R.id.txtDistance);
		txtMaxSpeed = findViewById(R.id.txtMaxSpeed);
		txtAverageSpeed = findViewById(R.id.txtAverageSpeed);
		tripListView = findViewById(R.id.tripListView);
	}
	
	/**
	 * Starts a new MapActivity sending in the requested trips to be drawn as intent extras
	 * @param view the view containing the button caller
	 */
	public void viewMapOnClick(View view) {
		// view the selected data on the map
		Intent tent = new Intent(this, MapsActivity.class);
		tent.putExtra("trips", getActiveTrips());
		startActivity(tent);
	}
	
	/**
	 * Updates the view with the selected/de-selected trip
	 * @param view the view containing the button caller
	 */
	public void setAddTripData(View view) {
		// get the calling button
		ToggleButton tog = (ToggleButton) view.getRootView();
		// get its trip
		Trip trip = (Trip) tog.getTag();
		
		if (tog.isSelected()) {
			// add data to view
			if (getActiveTrips().size() == 0) {
				// first trip entry
				setFirstTripData(trip);
			} else {
				// next trip entry
				addTripUpdateToView(trip);
			}
			getActiveTrips().add(trip);
		} else {
			// subtract data from view
			subtractTripUpdateToView(trip);
			getActiveTrips().remove(trip);
		}
		
		int newSize = getActiveTrips().size();
		if (newSize > 0) {
			setActiveTripAverageSpeedToView(newSize);
			setActiveTripMaxSpeedToView(newSize);
			setActiveTripDatesToView(newSize);
		} else {
			// 0 trips
			resetView();
		}
	}
	
	/**
	 * Resets the TextViews in the activity
	 */
	private void resetView() {
		getTxtAverageSpeed().setText("");
		getTxtMaxSpeed().setText("");
		getTxtDistance().setText("");
		getTxtDate().setText("");
		getTxtTime().setText("");
	}
	
	/**
	 * Updates the view with the currently selected trips
	 * @param size the current number of trips selected
	 */
	private void setActiveTripDatesToView(int size) {
		// get trips
		ArrayList<Trip> trips = getActiveTrips();
		
		if (size > 1) {
			Collections.sort(trips, Trip.Comparators.DATE);
			
			// get earliest date
			String earliest = trips.get(0).getDate().toString();
			
			// get latest date
			String latest = trips.get(trips.size() - 1).getDate().toString();
			
			// set date text to range
			String text = earliest + " - " + latest;
			getTxtDate().setText(text);
		} else {
			getTxtDate().setText(trips.get(0).getDate().toString());
		}
	}
	
	/**
	 * Sets the view using the data from the single trip
	 * @param trip the Trip to set the view with
	 */
	private void setFirstTripData(Trip trip) {
		getTxtDate().setText(trip.getDate().toString());
//		String timeString = formatTimeString((String)trip.getTimeObj(false));
//		getTxtTime().setText(timeString);
		getTxtDistance().setText(String.valueOf(trip.getDistance()));
		getTxtMaxSpeed().setText(String.valueOf(trip.getMaxSpeed()));
		getTxtAverageSpeed().setText(String.valueOf(trip.getAverageSpeed()));
	}
	
	/**
	 *
	 * @param timeObjString string representation of a time object in "hh:mm:ss" format
	 * @return "# Hr # Min # Sec" representation of the time for the view
	 */
	private String formatTimeString(String timeObjString) {
		String[] hms = timeObjString.split(":");
		return String.format("%1$s Hr %2$s Min %3$s Sec", hms[0], hms[1], hms[2]);
	}
	
	/**
	 * Adds the given trip data to the view
	 * @param trip the Trip to add to the view
	 */
	private void addTripUpdateToView(Trip trip) {
		// distance
		TextView txtDistance = getTxtDistance();
		float distance = Float.parseFloat(txtDistance.getText().toString());
		txtDistance.setText(String.valueOf(distance + trip.getDistance()));
		
		// time
		TextView txtTime = getTxtTime();
		// get in hh:mm:ss
//		Date tripTime = (Time) trip.getTimeObj(true);
		
		// get time obj from view
		Date oldTime = Time.valueOf(getJDBCTimeFormat(txtTime.getText().toString()));
//		long newMilis = oldTime.getTime() + tripTime.getTime();
		
		// create the new time to display to view
//		Time newTime = new Time(newMilis);
//		txtTime.setText(formatTimeString(newTime.toString()));
	}
	
	/**
	 * Subtracts the given trip data from the view
	 * @param trip the Trip to subtract from the view
	 */
	private void subtractTripUpdateToView(Trip trip) {
		// distance
		TextView txtDistance = getTxtDistance();
		float distance = Float.parseFloat(txtDistance.getText().toString());
		txtDistance.setText(String.valueOf(distance - trip.getDistance()));
		
		// time
		TextView txtTime = getTxtTime();
		// get in hh:mm:ss
//		Date tripTime = (Time) trip.getTimeObj(true);
		
		// get time obj from view
		Date oldTime = Time.valueOf(getJDBCTimeFormat(txtTime.getText().toString()));
//		long newMilis = oldTime.getTime() - tripTime.getTime();
		
		// create the new time to display to view
//		Time newTime = new Time(newMilis);
//		txtTime.setText(formatTimeString(newTime.toString()));
	}
	
	/**
	 * Converts time from the view format to standard format
	 * @param viewFormat time in the view format: "# Hr # Min # Sec"
	 * @return time in "hh:mm:ss" format
	 */
	private String getJDBCTimeFormat(String viewFormat) {
		// where viewFormat == "%1$s Hr %2$s Min %3$s Sec"
		// return as hh:mm:ss
		if (viewFormat != null) {
			viewFormat = viewFormat.replace(" Hr ", ":");
			viewFormat = viewFormat.replace(" Min ", ":");
			viewFormat = viewFormat.replace(" Sec", "");
		}

		// may be null
		return viewFormat;
	}
	
	/**
	 * Updates the view to notify the user that there are no trips to view
	 */
	private void setEmptyTripList() {
		TextView tv = new TextView(this);
		tv.setText(R.string.noTripsToView);
		tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
		tv.setTextSize(26.0f);
		getTripListView().addView(tv);
	}
	
	/**
	 * Sets the TextView representing the max speed
	 * @param size the current number of trips selected
	 */
	private void setActiveTripMaxSpeedToView(int size) {
		ArrayList<Trip> trips = getActiveTrips();
		
		if (size > 1) {
			Collections.sort(trips, Trip.Comparators.MAXSPEED);
			getTxtMaxSpeed().setText(String.valueOf(trips.get(size - 1).getMaxSpeed()));
		} else {
			getTxtMaxSpeed().setText(String.valueOf(trips.get(0).getMaxSpeed()));
		}
	}
	
	/**
	 * Sets the TextView representing the average speed
	 * @param size the current number of trips selected
	 */
	private void setActiveTripAverageSpeedToView(int size) {
		ArrayList<Trip> trips = getActiveTrips();
		if (size > 1) {
			float total = 0;
			
			for (Trip t : trips) {
				total += t.getAverageSpeed();
			}
			
			getTxtAverageSpeed().setText(String.valueOf(total / size));
		} else {
			getTxtAverageSpeed().setText(String.valueOf(trips.get(0).getAverageSpeed()));
		}
	}
	
	/**
	 * Fills the trips list view with toggle buttons representing each trip
	 * @param trips the list of trips to use
	 */
	private void buildTripList(ArrayList<Trip> trips) {
		for (Trip t : trips) {
			ToggleButton tog = new ToggleButton(this);
			String text = "Date:\t" + t.getDate().toString();
			text += "\nTime:\t" + t.getTime();
			text += "\nDistance:\t" + t.getDistance();
			tog.setText(text);
			tog.setTag(t);
//			tog.callOnClick(setAddTripData(tog)); // if listener doesn't work out
            tog.setOnClickListener(this::setAddTripData);
            getTripListView().addFooterView(tog, t, true);
        }
    }
	
	
	/*
	Getters and Setters
	*/

    public User getTheUser() {
        return theUser;
    }

    public void setTheUser(User theUser) {
        this.theUser = theUser;
    }

    public TextView getTxtDate() {
        return txtDate;
    }

    public TextView getTxtTime() {
        return txtTime;
    }

    public TextView getTxtDistance() {
        return txtDistance;
    }

    public TextView getTxtMaxSpeed() {
        return txtMaxSpeed;
    }

    public TextView getTxtAverageSpeed() {
        return txtAverageSpeed;
    }

    public ListView getTripListView() {
        return tripListView;
    }

    public ArrayList<Trip> getActiveTrips() {
        return activeTrips;
    }
}
