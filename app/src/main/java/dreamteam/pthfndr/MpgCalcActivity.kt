package dreamteam.pthfndr

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import dreamteam.pthfndr.models.User

class MpgCalcActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_mpg_calc)

        if (intent.extras != null) {

            val key = intent.extras!!.keySet().elementAt(0)
            // ..get the user - we should only get the user's data into the activity, nothing else
            val user = intent.extras!!.getParcelable<User>(key)

        }
    }
}