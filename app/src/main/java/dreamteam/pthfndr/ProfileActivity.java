package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dreamteam.pthfndr.models.FirebaseAccessor;
import dreamteam.pthfndr.models.User;

public class ProfileActivity extends AppCompatActivity {
    
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(".##");
    
    private User user;
    private TextView nameTextView;
    private TextView joinDateTextView;
    private TextView totalTripsTextView;
    private TextView totalTimeTextView;
    private TextView totalDistanceTextView;
    private TextView averageDistanceTextView;
    private TextView averageSpeedTextView;
    private TextView maxSpeedTextView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_profile);
        nameTextView = findViewById(R.id.txtName);
        joinDateTextView = findViewById(R.id.txtJoinDate);
        totalTripsTextView = findViewById(R.id.txtTotalTrips);
        totalTimeTextView = findViewById(R.id.txtTotalTime);
        totalDistanceTextView = findViewById(R.id.txtTotalDistance);
        averageDistanceTextView = findViewById(R.id.txtAverageDistance);
        averageSpeedTextView = findViewById(R.id.txtAverageSpeedOverall);
        maxSpeedTextView = findViewById(R.id.txtMaxSpeedOverall);
    }

    @Override
    public void onStart() {
        super.onStart();
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
    }

    public void signOutOnClick(View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                finalizeSignOut();
            }
        });
    }

    public void viewTripsOnClick(View view) {
        Intent tent = new Intent(this, TripHistoryActivity.class);
        tent.putExtra("user", user);
        startActivity(tent);
    }
    
    public void finalizeSignOut() {
        // this does not work properly?
        // ends up as a blank screen, needs to be the firebase oAuth screen
//        SigninActivity signIn = new SigninActivity();
//        signIn.signIn(null); // breaks on this line
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
//        startActivity(intent);
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
