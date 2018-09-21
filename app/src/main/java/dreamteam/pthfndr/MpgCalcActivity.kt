package dreamteam.pthfndr

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import dreamteam.pthfndr.models.FirebaseAccessor
import dreamteam.pthfndr.models.User
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class MpgCalcActivity : AppCompatActivity() {

    private val DECIMAL_FORMATTER = DecimalFormat(".##")
    private val DATE_FORMATTER = SimpleDateFormat("EEE, MMM d, yy", Locale.US)
    private lateinit var dateBox: TextView
    private lateinit var fillCostBox: TextView
    private lateinit var qtyBox: TextView
    private lateinit var avgMpgBox: TextView
    private lateinit var avgCostPerMileBox: TextView
    private lateinit var user: User
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpg_calc)

        val keys = intent.extras!!.keySet().toTypedArray()
        currentUser = intent.extras!!.getParcelable(keys[0])!!

        if (intent.extras != null) {
            val key = intent.extras!!.keySet().elementAt(0)
            // ..get the user - we should only get the user's data into the activity, nothing else
            user = intent.extras!!.getParcelable(key)!!

            // set text views
            dateBox = findViewById(R.id.lbl_date_data)
            fillCostBox = findViewById(R.id.lbl_cost_data)
            qtyBox = findViewById(R.id.lbl_qty_data)
            avgMpgBox = findViewById(R.id.lbl_miles_per_gallon_data)
            avgCostPerMileBox = findViewById(R.id.lbl_cost_per_mile_data)

            // set user's current data
            setDataToView()
        }
        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            menuItem.isChecked = true

            mDrawerLayout.closeDrawers()

            if (menuItem.itemId == R.id.nav_Profile) {
                val newIntent = Intent(this, ProfileActivity::class.java)
                newIntent.putExtra("user", currentUser)
                startActivity(newIntent)
            } else if (menuItem.itemId == R.id.nav_SignOut) {
                FirebaseAccessor.logout()
                val intent = Intent(this, SigninActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("user", currentUser)
                startActivity(intent)
            } else if (menuItem.itemId == R.id.nav_Mpgs) {
                val newIntent = Intent(this, MpgCalcActivity::class.java)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                newIntent.putExtra("user", currentUser)
                startActivity(newIntent)
            } else if (menuItem.itemId == R.id.nav_History) {
                val newIntent = Intent(this, TripHistoryActivity::class.java)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                newIntent.putExtra("user", currentUser)
                startActivity(newIntent)
            } //else if (menuItem.getItemId() == R.id.nav_Map) {
            //                        Intent newIntent = new Intent(this, MapsActivity.class);
            //                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //                        startActivity(newIntent);
            //                    }
            true
        }
    }

    private fun setDataToView() {
        // set the current data to views
        if (user.dateOfFillUp != 0.toLong()) {
            dateBox.text = DATE_FORMATTER.format(user.dateOfFillUp)
        } else {
            dateBox.text = "-"
        }

        fillCostBox.text = DECIMAL_FORMATTER.format(user.priceOfFillUp)
        qtyBox.text = DECIMAL_FORMATTER.format(user.gallonsOfGas)

        // set current averages to views
        avgMpgBox.text = DECIMAL_FORMATTER.format(user.averageMilesPerGallon)
        avgCostPerMileBox.text = DECIMAL_FORMATTER.format(user.averageCostPerMile)
    }

    fun btnUpdateOnClick(view: View?) {
        val newQty = findViewById<EditText>(R.id.txt_new_qty).text
        val newCost = findViewById<EditText>(R.id.txt_new_cost).text

        if (!newQty.isNullOrEmpty()) {
            user.gallonsOfGas = newQty.toString().toDouble()
        }

        if (!newCost.isNullOrEmpty()) {
            user.priceOfFillUp = newCost.toString().toDouble()
        }

        user.dateOfFillUp = System.currentTimeMillis()
        updateAverages(user.dateOfFillUp)
        setDataToView()
    }

    private fun updateAverages(lastFillDate: Long) {
        var totalDist = 0.0
        val lastDate = Date(lastFillDate)

        user.trips.forEach {
            if (it.date.after(lastDate)) {
                totalDist += it.distance.toDouble()
            }
        }

        user.averageMilesPerGallon = totalDist / user.gallonsOfGas
        user.averageCostPerMile = totalDist * user.averageMilesPerGallon
    }
}