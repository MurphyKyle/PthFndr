package dreamteam.pthfndr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import dreamteam.pthfndr.models.FirebaseAccessor;

public class SplashActivity extends AppCompatActivity {
	
	private static int i = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FirebaseAccessor.setFireBaseResources();
		FirebaseAccessor.setSignInClient(getApplicationContext());
		super.onCreate(savedInstanceState);
		Timer t = new Timer();
		
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(SplashActivity.this::toast);
				if (FirebaseAccessor.getUserModel() != null) {
					startActivity(new Intent(SplashActivity.this, SigninActivity.class));
					finish();
				}
			}
		}, 1000, 1000);
	}
	
	private void toast() {
		Toast.makeText(this, "Checking user.. try# " + i++, Toast.LENGTH_SHORT).show();
	}
}
