package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import reach52.marketplace.community.R
import kotlinx.android.synthetic.main.fragment_medicine_details.view.*

class MedicineDetailsFragment : androidx.fragment.app.Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.medicine_details)
        return inflater.inflate(R.layout.fragment_medicine_details, container, false).apply {
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }
}