package dreamteam.pthfndr;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dreamteam.pthfndr.models.FirebaseAccessor;
import dreamteam.pthfndr.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	private String mParam1;
	private String mParam2;
	
	private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");
	
	private User user;
	private TextView nameTextView;
	private TextView joinDateTextView;
	private TextView totalTripsTextView;
	private TextView totalTimeTextView;
	private TextView totalDistanceTextView;
	private TextView averageDistanceTextView;
	private TextView averageSpeedTextView;
	private TextView maxSpeedTextView;
	
	public ProfileFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ProfileFragment.
	 */
	public static ProfileFragment newInstance(String param1, String param2) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		nameTextView = view.findViewById(R.id.txtName);
		joinDateTextView = view.findViewById(R.id.txtJoinDate);
		totalTripsTextView = view.findViewById(R.id.txtTotalTrips);
		totalTimeTextView = view.findViewById(R.id.txtTotalTime);
		totalDistanceTextView = view.findViewById(R.id.txtTotalDistance);
		averageDistanceTextView = view.findViewById(R.id.txtAverageDistance);
		averageSpeedTextView = view.findViewById(R.id.txtAverageSpeedOverall);
		maxSpeedTextView = view.findViewById(R.id.txtMaxSpeedOverall);
		user = FirebaseAccessor.getUserModel();
		
		if (user != null) {
			nameTextView.setText(user.getName());
			Date date = new Date(FirebaseAccessor.getAuthUser().getMetadata().getCreationTimestamp());
			SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
			joinDateTextView.setText(df.format(date));
			int size = user.getTrips().size();
			totalTripsTextView.setText(DECIMAL_FORMATTER.format(size));
			totalTimeTextView.setText(DECIMAL_FORMATTER.format(getTotalTime(user)));
			totalDistanceTextView.setText(DECIMAL_FORMATTER.format(getTotalDistance(user)));
			averageDistanceTextView.setText(DECIMAL_FORMATTER.format(getAverageDistance(user)));
			maxSpeedTextView.setText(DECIMAL_FORMATTER.format(getMaxSpeed(user)));
			averageSpeedTextView.setText(DECIMAL_FORMATTER.format(getAverageSpeed(user)));
		}
		return view;
	}
	
	
	private float getTotalTime(User user) {
		float result = 0f;
		for (int i = 0; i < user.getTrips().size(); i++) {
			result += user.getTrips().get(i).getTime();
		}
		return result;
	}
	
	private float getTotalDistance(User user) {
		float result = 0f;
		for (int i = 0; i < user.getTrips().size(); i++) {
			result += user.getTrips().get(i).getDistance();
		}
		return result;
	}
	
	private float getAverageDistance(User user) {
		float totalDistance = 0f;
		for (int i = 0; i < user.getTrips().size(); i++) {
			// Trip.getDistance() will not do anything for us until it is fully implemented
			totalDistance += user.getTrips().get(i).getDistance();
		}
		float averageDistance = totalDistance / user.getTrips().size();
		
		return averageDistance;
	}
	
	private float getMaxSpeed(User user) {
		float result = 0f;
		for (int i = 0; i < user.getTrips().size(); i++) {
			if (user.getTrips().get(i).getMaxSpeed() > result) {
				result = user.getTrips().get(i).getMaxSpeed();
			}
		}
		return result;
	}
	
	private float getAverageSpeed(User user) {
		float speed = 0f;
		for (int i = 0; i < user.getTrips().size(); i++) {
			speed += user.getTrips().get(i).getAverageSpeed();
		}
		float result = speed / user.getTrips().size();
		return result;
	}
}
