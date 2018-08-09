package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//                FirebaseDatabase fDB = FirebaseDatabase.getInstance();
//                DatabaseReference fRef = fDB.getReference("users");
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
}