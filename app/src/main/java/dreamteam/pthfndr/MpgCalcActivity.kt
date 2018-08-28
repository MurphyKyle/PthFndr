package dreamteam.pthfndr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import dreamteam.pthfndr.models.User
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class MpgCalcActivity:AppCompatActivity() {

    private val DECIMAL_FORMATTER = DecimalFormat(".##")
    private val DATE_FORMATTER = SimpleDateFormat("EEE, MMM d, yy", Locale.US)
    private lateinit var dateBox: TextView
    private lateinit var fillCostBox: TextView
    private lateinit var qtyBox: TextView
    private lateinit var avgMpgBox: TextView
    private lateinit var avgCostPerMileBox: TextView
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpg_calc)

        if (intent.extras != null) {
            val key = intent.extras!!.keySet().elementAt(0)
            // ..get the user - we should only get the user's data into the activity, nothing else
            user = intent.extras!!.getParcelable<User>(key)

            // set text views
            dateBox = findViewById(R.id.lbl_date_data)
            fillCostBox = findViewById(R.id.lbl_cost_data)
            qtyBox = findViewById(R.id.lbl_qty_data)
            avgMpgBox = findViewById(R.id.lbl_miles_per_gallon_data)
            avgCostPerMileBox = findViewById(R.id.lbl_cost_per_mile_data)

            // set user's current data
            setDataToView()
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