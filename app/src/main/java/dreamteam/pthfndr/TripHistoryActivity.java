package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import dreamteam.pthfndr.models.Trip;
import dreamteam.pthfndr.models.User;

public class TripHistoryActivity extends AppCompatActivity {
	
	private User theUser;
	private TextView txtDate = findViewById(R.id.txtDate);
	private TextView txtTime = findViewById(R.id.txtTime);
	private TextView txtDistance = findViewById(R.id.txtDistance);
	private TextView txtMaxSpeed = findViewById(R.id.txtMaxSpeed);
	private TextView txtAverageSpeed = findViewById(R.id.txtAverageSpeed);
	private ScrollView tripListView = findViewById(R.id.tripListView);
	
	private ArrayList<Trip> activeTrips = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// ignoring what the parcel is actually called..
		String[] keys = getIntent().getExtras().keySet().toArray(new String[1]);
		// ..get the user - we should only get the user's data into the activity, nothing else
		setTheUser(getIntent().getExtras().getParcelable(keys[0]));
		ArrayList<Trip> trips = getTheUser().getTrips();
		
		if (trips.size() > 0) {
			// build the list of selectable trips
			buildTripList(trips);
		} else {
			// notify if the user has no trips to view
			setEmptyTripList();
		}
	}
	
	public void viewMapOnClick(View view) {
		// view the selected data on the map
		Intent tent = new Intent(this, MapsActivity.class);
		tent.putExtra("trips", getActiveTrips());
		startActivity(tent);
	}
	
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
				addTripData(trip);
			}
			getActiveTrips().add(trip);
		} else {
			// subtract data from view
			subtractTripData(trip);
			getActiveTrips().remove(trip);
		}
		
		updateTripDates();
	}
	
	private void updateTripDates() {
		// get trips
		ArrayList<Trip> trips = getActiveTrips();
		//Collections.sort(trips, Trip.Comparators.DATE);
		
		// get earliest date
		String earliest = trips.get(0).getDate().toString();
		
		// get latest date
		String latest = trips.get(trips.size() - 1).getDate().toString();
		
		// set date text to range
		String text = earliest + " - " + latest;
		getTxtDate().setText(text);
	}
	
	private void setFirstTripData(Trip trip) {
		getTxtDate().setText(trip.getDate().toString());
		String timeString = getTimeString(trip);
		getTxtTime().setText(timeString);
		getTxtDistance().setText(String.valueOf(trip.getDistance()));
		getTxtMaxSpeed().setText(String.valueOf(trip.getMaxSpeed()));
		getTxtAverageSpeed().setText(String.valueOf(trip.getAverageSpeed()));
	}
	
	private String getTimeString(Trip trip) {
		
		String time = null;//(String)trip.getTimeObj(true);
		String[] hms = time.split(":");
		return String.format("%1$s Hr %2$s Min  %3$s Sec", hms[0], hms[1], hms[2]);
	}
	
	private void addTripData(Trip trip) {
		TripData.updateModel(this);
		TripData.setDistance(trip.getDistance() + TripData.getDistance());
		TripData.setMaxSpeed(trip.getMaxSpeed() + TripData.getMaxSpeed());
		TripData.setAverageSpeed(trip.getAverageSpeed() + TripData.getAverageSpeed());
		TripData.updateView(this);
	}
	
	private void subtractTripData(Trip trip) {
		TripData.updateModel(this);
		TripData.setDistance(TripData.getDistance() - trip.getDistance());
		TripData.setMaxSpeed(getActiveTripMaxSpeed());
		TripData.setAverageSpeed(getActiveTripAverageSpeed());
		TripData.updateView(this);
	}
	
	private void setEmptyTripList() {
		TextView tv = new TextView(this);
		tv.setText(R.string.noTripsToView);
		getTripListView().addView(tv);
	}
	
	private float getActiveTripMaxSpeed() {
		ArrayList<Trip> trips = getActiveTrips();
		int size = trips.size();
		//Collections.sort(trips, Trip.Comparators.MAXSPEED);
		return trips.get(size - 1).getMaxSpeed();
	}
	
	private float getActiveTripAverageSpeed() {
		ArrayList<Trip> trips = getActiveTrips();
		int size = trips.size();
		double total = 0;
		
		for (Trip t : trips) {
			total += t.getAverageSpeed();
		}
		
		return (float) (total / size);
	}
	
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
			getTripListView().addView(tog);
		}
	}
	
	
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
	
	public ScrollView getTripListView() {
		return tripListView;
	}
	
	public ArrayList<Trip> getActiveTrips() {
		return activeTrips;
	}
	
	/**
	 * Helper class to update the view easier
	 */
	private static class TripData {
		private static String time;
		private static String distance;
		private static String maxSpeed;
		private static String averageSpeed;
		
		public static void updateModel(TripHistoryActivity src) {
			time = (String) src.getTxtTime().getText();
			distance = (String) src.getTxtDistance().getText();
			maxSpeed = (String) src.getTxtMaxSpeed().getText();
			averageSpeed = (String) src.getTxtAverageSpeed().getText();
		}
		
		public static void updateView(TripHistoryActivity src) {
			src.getTxtDistance().setText(distance);
			src.getTxtMaxSpeed().setText(maxSpeed);
			src.getTxtAverageSpeed().setText(averageSpeed);
		}
		
		public static double getDistance() {
			return Double.parseDouble(distance);
		}
		
		public static float getMaxSpeed() {
			return Float.parseFloat(maxSpeed);
		}
		
		public static double getAverageSpeed() {
			return Double.parseDouble(averageSpeed);
		}
		
		public static void setDistance(double distance) {
			TripData.distance = String.valueOf(distance);
		}
		
		public static void setMaxSpeed(float maxSpeed) {
			TripData.maxSpeed = String.valueOf(maxSpeed);
		}
		
		public static void setAverageSpeed(double averageSpeed) {
			TripData.averageSpeed = String.valueOf(averageSpeed);
		}
		
	}
	
}
