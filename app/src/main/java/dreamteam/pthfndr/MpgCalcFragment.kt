package dreamteam.pthfndr

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import dreamteam.pthfndr.models.FirebaseAccessor
import dreamteam.pthfndr.models.User
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [MpgCalcFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MpgCalcFragment : Fragment(), View.OnClickListener {
    private val DECIMAL_FORMATTER = DecimalFormat("#.##")
    private val DATE_FORMATTER = SimpleDateFormat("EEE, MMM d, yy", Locale.US)
    private lateinit var thisView: View
    private lateinit var dateBox: TextView
    private lateinit var fillCostBox: TextView
    private lateinit var qtyBox: TextView
    private lateinit var avgMpgBox: TextView
    private lateinit var avgCostPerMileBox: TextView
    private lateinit var updateButton: Button
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAccessor.getUserModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = layoutInflater.inflate(R.layout.fragment_mpg_calc, null)
        dateBox = thisView.findViewById(R.id.lbl_date_data)
        fillCostBox = thisView.findViewById(R.id.lbl_cost_data)
        qtyBox = thisView.findViewById(R.id.lbl_qty_data)
        avgMpgBox = thisView.findViewById(R.id.lbl_miles_per_gallon_data)
        avgCostPerMileBox = thisView.findViewById(R.id.lbl_cost_per_mile_data)
        updateButton = thisView.findViewById(R.id.btn_update_fill_data)
        updateButton.setOnClickListener(this)
        // set user's current data
        setDataToView()
        return thisView
    }

    override fun onClick(p0: View?) {
        btnUpdateOnClick(p0)
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

    private fun btnUpdateOnClick(view: View?) {
        val newQty = thisView.findViewById<EditText>(R.id.txt_new_qty).text
        val newCost = thisView.findViewById<EditText>(R.id.txt_new_cost).text

        if (!newQty.isNullOrEmpty()) {
            user.gallonsOfGas = newQty.toString().toDouble()
        }

        if (!newCost.isNullOrEmpty()) {
            user.priceOfFillUp = newCost.toString().toDouble()
        }

        user.dateOfFillUp = System.currentTimeMillis()
        // TODO: cannot send the NEW DATE as the OLD DATE
        updateAverages(user.dateOfFillUp)
        setDataToView()
    }

    private fun updateAverages(lastFillDate: Long) {
        var totalDist = 0.0
        val lastDate = Date(lastFillDate)

        // TODO: sending the NEW DATE in here makes this foreach not work
        user.trips.forEach {
            if (it.date.after(lastDate)) {
                totalDist += it.distance.toDouble()
            }
        }

        user.averageMilesPerGallon = totalDist / user.gallonsOfGas
        user.averageCostPerMile = totalDist * user.averageMilesPerGallon
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MpgCalcFragment.
         */
        @JvmStatic
        fun newInstance() = MpgCalcFragment()
    }
}
