package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
    private DrawerLayout mDrawerLayout;
    private User currentUser;

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

        String[] keys = getIntent().getExtras().keySet().toArray(new String[1]);
        currentUser = getIntent().getExtras().getParcelable(keys[0]);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                (MenuItem menuItem) -> {
                    menuItem.setChecked(true);

                    mDrawerLayout.closeDrawers();

                    if (menuItem.getItemId() == R.id.nav_Profile) {
                        Intent newIntent = new Intent(this, ProfileActivity.class);
                        newIntent.putExtra("user", currentUser);
                        startActivity(newIntent);
                    } else if (menuItem.getItemId() == R.id.nav_SignOut) {
                        FirebaseAccessor.logout();
                        Intent intent = new Intent(this, SigninActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                    } else if (menuItem.getItemId() == R.id.nav_Mpgs) {
                        Intent newIntent = new Intent(this, MpgCalcActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.putExtra("user", currentUser);
                        startActivity(newIntent);
                    } else if (menuItem.getItemId() == R.id.nav_History) {
                        Intent newIntent = new Intent(this, TripHistoryActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.putExtra("user", currentUser);
                        startActivity(newIntent);
                    } //else if (menuItem.getItemId() == R.id.nav_Map) {
//                        Intent newIntent = new Intent(this, MapsActivity.class);
//                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(newIntent);
//                    }
                    return true;
                });
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
        FirebaseAccessor.logout();
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
