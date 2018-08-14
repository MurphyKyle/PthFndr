package dreamteam.pthfndr;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dreamteam.pthfndr.models.Path;
import dreamteam.pthfndr.models.Trip;
import dreamteam.pthfndr.models.User;


public class SigninActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.EmailBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signIn(null);
        //setContentView(R.layout.activity_signin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK && data != null) {
                final DatabaseReference fDB = FirebaseDatabase.getInstance().getReference().child("users");
                fDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (!snapshot.child(user.getUid()).exists()) {
                            User newUser = new User();
                            newUser.setName(user.getDisplayName());
                            newUser.setUID(user.getUid());
                           newUser.add_trip(randomTrip());
                           newUser.add_trip(randomTrip());
                            newUser.add_trip(randomTrip());
                            Map<String, Object> userValues = newUser.toMap();
                            Map<String, Object> userUpdates = new HashMap<>();
                            userUpdates.put("/"+newUser.getUID()+"/", userValues);
                            fDB.updateChildren(userUpdates);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //This code adds trips to the user for test data
//                            newUser.add_trip(randomTrip());
//                            newUser.add_trip(randomTrip());
//                            newUser.add_trip(randomTrip());



//                User u = new User(user.getDisplayName(), user.getUid());
//                Trip t = new Trip(null);
//                t.paths.add(new Path(new Location("bye"), null, null, 50));
//                u.add_trip(t);
//                u.add_trip(t);
//                u.add_trip(t);
//                u.add_trip(t);
//                fRef.child(u.getUID()).setValue(u);

                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
            } else {
                try {
                    Toast.makeText(this, response.getError().getErrorCode(), Toast.LENGTH_LONG).show();
                } catch (NullPointerException e) {
                    Toast.makeText(this, "Unknown Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void signIn(View view) {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.pth_fndr_logo1png)
                .setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar).setAvailableProviders(providers).build(), RC_SIGN_IN);
    }

    private Trip randomTrip(){

        Trip trip = new Trip();
        trip.setAverageSpeed(generateRandomDouble(100));
        trip.setDistance(generateRandomDouble(1000));
        trip.setMaxSpeed(generateRandomFloat(200));
        trip.setTime(generateRandomDouble(100));
        trip.paths.add(new Path(new Location("bye"), null, (int)generateRandomDouble(100), new Random().nextInt(100)));
        return trip;
    }

    private float generateRandomFloat(float max){
        float leftLimit = 1F;
        float rightLimit = max;
        float generatedFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
        return generatedFloat;
    }
    private double generateRandomDouble(double max){
        double leftLimit = 1D;
        double rightLimit = max;
        double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
        return generatedDouble;
    }

}