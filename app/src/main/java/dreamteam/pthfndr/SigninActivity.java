package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import java.util.List;

import dreamteam.pthfndr.models.FirebaseAccessor;
import dreamteam.pthfndr.models.User;

public class SigninActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static SigninActivity thisRef;
    List<AuthUI.IdpConfig> providers
            = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.EmailBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAccessor.setFireBaseResources();
        FirebaseAccessor.setSignInClient(getApplicationContext());
        super.onCreate(savedInstanceState);
        thisRef = this;
        signIn(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // i think i'm logged in if i get to this line
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK && data != null) {
                // try to get the FirebaseAccess user
                User newUser = null;
                if (FirebaseAccessor.getUserModel() == null) {
                    // no current user in the db
                    newUser = new User();
                    // create a new user in the pthfndr database
                    if (FirebaseAccessor.createUserModel(newUser)) {
                        Toast.makeText(thisRef, "New User Saved!", Toast.LENGTH_LONG).show();
                    }
                }
                Intent i = new Intent(this, MapsActivity.class);
                i.putExtra("user", newUser);
                startActivity(i);
                Intent i2 = new Intent(this, MapsIntentService.class);
                startService(i2);
            } else {
                try {
                    Toast.makeText(this, response.getError().getErrorCode(), Toast.LENGTH_LONG).show();
                    finish();
                } catch (NullPointerException e) {
                    Toast.makeText(this, "Unknown Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void signIn(View view) {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.pth_fndr_logo1png)
                .setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar)
                .setAvailableProviders(providers)
                .build().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), RC_SIGN_IN);
    }
}