package dreamteam.pthfndr.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class FirebaseAccessor {
	/**
	 * Currently authenticated Firebase User
	 */
	private static FirebaseUser AUTH_USER;
	/**
	 * The Firebase reference to the authenticated user's record
	 */
	private static DatabaseReference FIREBASE;
	
	/**
	 * The current PthFndr User model
	 */
	private static User USER_MODEL;

	public static void setFireBaseResources() {
		// get the users 'table' from the DB instance
		FIREBASE = FIREBASE == null ? FirebaseDatabase.getInstance().getReference().child("users") : FIREBASE;
		
		// get the current firebase auth user
		AUTH_USER = AUTH_USER == null ? FirebaseAuth.getInstance().getCurrentUser() : AUTH_USER;
		
		// adds the single value event listener to get the current firebase PthFndr User
		if (USER_MODEL == null) {
			getUserModelSnapshot();
		}

//        LinearLayout content = findViewById(R.id.password_layout);
//        View v = View.inflate(content.getContext(), mapFragment.getId(), content);
//        content.updateViewLayout(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.);
	}
	
	public static FirebaseUser getAuthUser() {
		setFireBaseResources();
		return AUTH_USER;
	}
	
	/**
	 * Gets the pthfndr.models.User for the current authenticated user
	 * @return The most current PthFndr User model from the database or null
	 */
	public static User getUserModel() {
		setFireBaseResources();
		return USER_MODEL;
	}
	
	/**
	 * Creates a new user model in the database with authenticated user's uid
	 * @param userModel The user model to use
	 * @return true or false success result
	 */
	public static boolean createUserModel(User userModel) {
		if (userModel == null) {
			userModel = new User();
		}
		
		return updateUserModel(userModel);
	}
	
	/**
	 * Updates the database of the given user model
	 * @param userModel The user model to use
	 * @return true success result or throws exception
	 */
	public static boolean updateUserModel(User userModel) {
		if (userModel == null) {
			return createUserModel(userModel);
		} else {
			setFireBaseResources();
		
			if (userModel.getName().equals(User.DEFAULT_NAME)) {
				userModel.setName(AUTH_USER.getDisplayName());
				userModel.setUID(AUTH_USER.getUid());
			}
			
			// get model data
			Map<String, Object> userValue = userModel.toMap();
			Map<String, Object> firebaseChild = new HashMap<>();
			
			// if the user is not in the DB, it is a create, not update
			// if the user is in the DB, it is an update, not create
			boolean createUser = USER_MODEL == null;
			String userKey = AUTH_USER.getUid();
			
			// set the data
			firebaseChild.put(userKey, userValue);
			
			// update the db
			DatabaseReference.CompletionListener completionListener = (databaseError, databaseReference) -> {
				if (databaseError != null) {
					throw databaseError.toException();
				}
			};
			
			if (createUser) {
				FIREBASE.setValue(firebaseChild, completionListener);
			} else {
				FIREBASE.updateChildren(firebaseChild, completionListener);
			}
			
			// set the model, don't wait for the firebase task
			USER_MODEL = userModel;
		}
		
		return true;
	}
	
	private static void getUserModelSnapshot() {
		// get the current firebase PthFndr User
		FIREBASE.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				USER_MODEL = snapshot.child(AUTH_USER.getUid()).getValue(dreamteam.pthfndr.models.User.class);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}
}
