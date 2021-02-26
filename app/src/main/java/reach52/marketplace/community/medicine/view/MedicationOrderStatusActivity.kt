package reach52.marketplace.community.medicine.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.medicine.viewmodel.OrdersViewModel
import reach52.marketplace.community.resident.view.ResidentDetailsActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.Exception


class MedicationOrderStatusActivity : BaseActivity() {

    companion object {
        const val KEY_RESIDENT_ID = "res_id"
    }

    private lateinit var vm: OrdersViewModel
    private lateinit var menu: Menu
    private lateinit var id: String
    private val purchasedMedicationListFragment by lazy { PurchasedMedicationListFragment() }
    private val medicineProductDetailsFragment by lazy { PurchaseDetailsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication_purchase_status)
        setSupportActionBar(toolbar)

        id = intent.getStringExtra(MedicationPurchaseActivity.KEY_RESIDENT_ID)

        vm = ViewModelProvider(this)[OrdersViewModel::class.java]
        vm.residentId = id
        gotoFragment(purchasedMedicationListFragment, true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1)
            supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
        super.onBackPressed()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.insurance_purchase, menu)
        this.menu = menu;


        /*	menu.findItem(R.id.action_homee).actionView.setOnClickListener {
                try {
                    System.out.println("rclick1= ")
                }catch (e: java.lang.Exception)
                {
                    e.message
                }
            }*/
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)



        menu.findItem(R.id.home_action).let {
            it.isVisible = true

            it.setOnMenuItemClickListener {


                    /* (this as InsurancePurchaseActivity).gotoOrderPreviewhome(this)*/

                    try {
                        startActivity(Intent(this, ResidentDetailsActivity::class.java).apply {
                            this.putExtra(ResidentDetailsActivity.KEY_ID, id)
                            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        })
                        finish()
                    } catch (e: Exception) {
                        System.out.println("onclick home= " + e.message)
                    }

                true
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    // click on toolbar backpress arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun goToMedicationProductDetailsFragment(){
        gotoFragment(medicineProductDetailsFragment, emptyStack = false, addToBackStack = true)
    }

}