package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

import dreamteam.pthfndr.models.User;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser authUser;
    private DatabaseReference mUserReference;
    private User user;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_profile);
        authUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(authUser.getUid());
    }

    @Override
    public void onStart(){
        super.onStart();
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

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

    public void finalizeSignOut(){
        Intent intent = new Intent(this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        startActivity(intent);
    }
    // this method gets the current user from the database and sets the data inside of the user object
    private User getUserFromDataBase(FirebaseUser authUser){
        User user = new User();

        return user;
    }
}
