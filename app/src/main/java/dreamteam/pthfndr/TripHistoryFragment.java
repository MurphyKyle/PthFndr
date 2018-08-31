package dreamteam.pthfndr;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dreamteam.pthfndr.models.FirebaseAccessor;
import dreamteam.pthfndr.models.Trip;
import dreamteam.pthfndr.models.User;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripHistoryFragment extends Fragment {
	protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM d, yy", Locale.US);
	protected static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");
	
	private User theUser;
	private TextView txtDate;
	private TextView txtTime;
	private TextView txtDistance;
	private TextView txtMaxSpeed;
	private TextView txtAverageSpeed;
	private ListView tripListView;
	private ArrayList<Trip> activeTrips = new ArrayList<>();
	
	
	public TripHistoryFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment TripHistoryFragment.
	 */
	public static TripHistoryFragment newInstance(String param1, String param2) {
		return new TripHistoryFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_trip_history, container, false);
		// set the view
		setViewObjects(view);
		// set the view children
		resetView();
		
		setTheUser(FirebaseAccessor.getUserModel());
			ArrayList<Trip> trips = getTheUser().getTrips();
			
			if (trips.size() > 0) {
				// build the list of selectable trips
				//buildTripList(trips);
				TripHistoryFragment.TripAdapter adapter =
						new TripHistoryFragment.TripAdapter(this.getActivity(), R.layout.trip_view_row, trips);
				tripListView.setAdapter(adapter);
			} else {
				// notify if the user has no trips to view
//				setEmptyTripList();
			}
//		}
		return view;
	}
	
	private void setViewObjects(View view) {
		txtDate = view.findViewById(R.id.txtDate);
		txtTime = view.findViewById(R.id.txtTime);
		txtDistance = view.findViewById(R.id.txtDistance);
		txtMaxSpeed = view.findViewById(R.id.txtMaxSpeed);
		txtAverageSpeed = view.findViewById(R.id.txtAverageSpeed);
		tripListView = view.findViewById(R.id.tripListView);
	}
	
	/**
	 * Updates the view with the selected/de-selected trip
	 * @param view the view containing the button caller
	 */
	public void updateTripData(View view) {
		// get the calling button
		ConstraintLayout layout = (ConstraintLayout) view;
		// get its trip
		TripHistoryFragment.TripAdapter.ViewHolder holder = (TripHistoryFragment.TripAdapter.ViewHolder) layout.getTag();
		holder.isSelected = !holder.isSelected;
		Trip trip = holder.trip;
		
		if (holder.isSelected) {
			// add data to view
			layout.setBackgroundColor(getResources().getColor(R.color.colorActiveTrip));
			if (getActiveTrips().size() == 0) {
				// first trip entry
				setFirstTripData(trip);
			} else {
				// next trip entry
				addTripUpdateToView(trip);
			}
			getActiveTrips().add(trip);
		} else {
			// subtract data from view if there are more than 1 trip
			if (getActiveTrips().size() - 1 > 0) {
				subtractTripUpdateToView(trip);
			}
			layout.setBackgroundColor(getResources().getColor(R.color.colorInactiveTrip));
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
		getTxtAverageSpeed().setText("-");
		getTxtMaxSpeed().setText("-");
		getTxtDistance().setText("-");
		getTxtDate().setText("-");
		getTxtTime().setText("-");
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
			Date earliest = trips.get(0).getDate();
			
			// get latest date
			Date latest = trips.get(trips.size() - 1).getDate();
			
			if (DATE_FORMATTER.format(earliest).equals(DATE_FORMATTER.format(latest))) {
				getTxtDate().setText(DATE_FORMATTER.format(trips.get(0).getDate()));
			} else {
				// set date text to range
				String text = DATE_FORMATTER.format(earliest)+ " - " + DATE_FORMATTER.format(latest);
				getTxtDate().setText(text);
			}
		} else {
			getTxtDate().setText(DATE_FORMATTER.format(trips.get(0).getDate()));
		}
	}
	
	/**
	 * Sets the view using the data from the single trip
	 * @param trip the Trip to set the view with
	 */
	private void setFirstTripData(Trip trip) {
		getTxtDate().setText(DATE_FORMATTER.format(trip.getDate()));
//		String timeString = formatTimeString((String)trip.getTimeObj(false));
		getTxtTime().setText(DECIMAL_FORMATTER.format(trip.getTime()));
		getTxtDistance().setText(DECIMAL_FORMATTER.format(trip.getDistance()));
		getTxtMaxSpeed().setText(DECIMAL_FORMATTER.format(trip.getMaxSpeed()));
		getTxtAverageSpeed().setText(DECIMAL_FORMATTER.format(trip.getAverageSpeed()));
	}
	
	/**
	 * Adds the given trip data to the view
	 * @param trip the Trip to add to the view
	 */
	private void addTripUpdateToView(Trip trip) {
		// distance
		TextView txtDistance = getTxtDistance();
		float distance = Float.parseFloat(txtDistance.getText().toString());
		txtDistance.setText(DECIMAL_FORMATTER.format(distance + trip.getDistance()));
		
		// time
		float currentTime = Float.parseFloat( (String)getTxtTime().getText() );
		float newTime = currentTime + trip.getTime();
		getTxtTime().setText(DECIMAL_FORMATTER.format(newTime));
	}
	
	/**
	 * Subtracts the given trip data from the view
	 * @param trip the Trip to subtract from the view
	 */
	private void subtractTripUpdateToView(Trip trip) {
		// distance
		TextView txtDistance = getTxtDistance();
		float distance = Float.parseFloat(txtDistance.getText().toString());
		txtDistance.setText(DECIMAL_FORMATTER.format(distance - trip.getDistance()));
		
		float currentTime = Float.parseFloat( (String)getTxtTime().getText() );
		float newTime = currentTime - trip.getTime();
		getTxtTime().setText(DECIMAL_FORMATTER.format(newTime));
	}
	
	/**
	 * Updates the view to notify the user that there are no trips to view
	 */
//	private void setEmptyTripList() {
//		TextView tv = new TextView(this);
//		tv.setText(R.string.noTripsToView);
//		tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//		tv.setTextSize(26.0f);
//		getTripListView().addView(tv);
//	}
	
	/**
	 * Sets the TextView representing the max speed
	 * @param size the current number of trips selected
	 */
	private void setActiveTripMaxSpeedToView(int size) {
		ArrayList<Trip> trips = getActiveTrips();
		
		if (size > 1) {
			Collections.sort(trips, Trip.Comparators.MAXSPEED);
			getTxtMaxSpeed().setText(DECIMAL_FORMATTER.format(trips.get(size - 1).getMaxSpeed()));
		} else {
			getTxtMaxSpeed().setText(DECIMAL_FORMATTER.format(trips.get(0).getMaxSpeed()));
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
			
			getTxtAverageSpeed().setText(DECIMAL_FORMATTER.format(total / size));
		} else {
			getTxtAverageSpeed().setText(DECIMAL_FORMATTER.format(trips.get(0).getAverageSpeed()));
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
	
	
	protected class TripAdapter extends ArrayAdapter<Trip> {
		private final Activity context;
		private final ArrayList<Trip> trips;
		
		class ViewHolder {
			private Trip trip;
			private boolean isSelected = false;
			private ConstraintLayout tog;
			public TextView date;
			public TextView time;
			private TextView distance;
		}
		
		private TripAdapter(@NonNull Activity context, int resource, @NonNull List<Trip> objects) {
			super(context, resource, objects);
			this.context = context;
			this.trips = (ArrayList<Trip>) objects;
		}
		
		@Override
		public int getCount() {
			return this.trips.size();
		}
		
		@Override
		public Trip getItem(int i) {
			return this.trips.get(i);
		}
		
		@Override
		public long getItemId(int i) {
			return 0;
		}
		
		@Override
		@NonNull
		public View getView(int position, View convertView, @NonNull ViewGroup parent) {
			View rowView = convertView;
			// reuse views
			if (rowView == null) {
				// get activity infater
				android.view.LayoutInflater inflater = context.getLayoutInflater();
				
				// inflate the list view row
				rowView = inflater.inflate(R.layout.trip_view_row, null);
				
				// configure view holder - acts as a cache for the view data
				TripHistoryFragment.TripAdapter.ViewHolder viewHolder = new TripHistoryFragment.TripAdapter.ViewHolder();
				viewHolder.tog = rowView.findViewById(R.id.trip_button);
				viewHolder.tog.setBackgroundColor(getResources().getColor(R.color.colorInactiveTrip));
				viewHolder.tog.setOnClickListener(TripHistoryFragment.this::updateTripData);
				viewHolder.date = rowView.findViewById(R.id.trip_date);
				viewHolder.time = rowView.findViewById(R.id.trip_time);
				viewHolder.distance = rowView.findViewById(R.id.trip_distance);
				rowView.setTag(viewHolder);
			}
			
			// fill data
			TripHistoryFragment.TripAdapter.ViewHolder holder = (TripHistoryFragment.TripAdapter.ViewHolder) rowView.getTag();
			Trip t = trips.get(position);
			holder.trip = t;
			holder.date.setText(DATE_FORMATTER.format(t.getDate()));
			holder.time.setText(DECIMAL_FORMATTER.format(t.getTime()));
			holder.distance.setText(DECIMAL_FORMATTER.format(t.getDistance()));
			
			return rowView;
		}
	}
}
