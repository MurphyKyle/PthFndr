package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SigninActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.Theme_AppCompat_DayNight_DarkActionBar)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase fDB = FirebaseDatabase.getInstance();
                DatabaseReference fRef = fDB.getReference("users");
                fRef.setValue("dkfdjf");
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
//                     Toast.makeText(this,response.getError().getErrorCode(), Toast.LENGTH_LONG ).show();
                //and handle the error.
                // ...
            }
        }
    }
}




