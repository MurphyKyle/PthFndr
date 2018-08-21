package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dreamteam.pthfndr.models.User;

public class ProfileActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView joinDateTextView;
    TextView totalTripsTextView;
    TextView totalTimeTextView;
    TextView totalDistanceTextView;
    TextView averageDistanceTextView;
    TextView averageSpeedTextView;
    TextView maxSpeedTextView;
    private FirebaseUser authUser;
    private DatabaseReference mUserReference;
    private User user;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_profile);
        authUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(authUser.getUid());
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
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                nameTextView.setText(user.getName());
                Date date = new Date(authUser.getMetadata().getCreationTimestamp());
                SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
                joinDateTextView.setText(df.format(date));
                int size = user.getTrips().size();
                totalTripsTextView.setText(String.valueOf(size));
                totalTimeTextView.setText(String.valueOf(getTotalTime(user)));
                totalDistanceTextView.setText(String.valueOf(getTotalDistance(user)));
                averageDistanceTextView.setText(String.valueOf(getAverageDistance(user)));
                maxSpeedTextView.setText(String.valueOf(getMaxSpeed(user)));
                averageSpeedTextView.setText(String.valueOf(getAverageSpeed(user)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Bad", "loadFailed:onCanceled", databaseError.toException());
            }
        });
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
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
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
