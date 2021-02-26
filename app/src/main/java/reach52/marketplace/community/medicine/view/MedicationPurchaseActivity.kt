package reach52.marketplace.community.medicine.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import reach52.marketplace.community.BuildConfig
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.resident.view.ResidentDetailsActivity
import kotlinx.android.synthetic.main.app_bar_main.*


class MedicationPurchaseActivity: BaseActivity(), MenuItem.OnActionExpandListener {
    companion object {
        const val KEY_RESIDENT_ID = "res_id"
    }
    var selecteMedsCount = 0
    private lateinit var menu : Menu
    private lateinit var  cartitem: MenuItem
    private lateinit var vm: MedicationPurchaseViewModel
    private val consentFragment by lazy { MedicationConsentFragment() }
    private val orderExtraDataFragment by lazy { OrderExtraDataFragment() }
    private val prescriptionFragment by lazy { PrescriptionFragment() }
    private val medicationSupplierFragment by lazy { MedicationSupplierFragment() }
    private val medicationBasketFragment by lazy { OrderCartFragment() }
    private val medicineListFragment by lazy { MedicineListFragment() }
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_purchase)
        setSupportActionBar(toolbar)

        id = intent.getStringExtra(KEY_RESIDENT_ID)
       /* val keyse = SharedPrefs.readString(SharedPrefs.KEY_SESSION)
        Log.d("sessien= ",keyse.toString());*/

        vm = ViewModelProvider(this)[MedicationPurchaseViewModel::class.java]
        dispo.add(vm.loadResident(this, id).subscribe {
            gotoFragment(consentFragment, true)
        })
    }

    fun dispatchTakePictureIntent(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {

            val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.FileProvider", vm.prescriptionImageFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(takePictureIntent, requestCode)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == orderExtraDataFragment.REQUEST_IMAGE_CAPTURE) {
            prescriptionFragment.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        this.menu= menu
        menu.findItem(R.id.app_bar_search).let {
            it.setOnActionExpandListener(this)
            dispo.add((it.actionView as SearchView).queryTextChanges().subscribe({ charSequence ->
                Search.setQuery(charSequence.toString())
            }, {
                throw NotImplementedError()
            }))
        }
        menu.findItem(R.id.action_cart).let {

            this.cartitem = menu.findItem(R.id.action_cart)

            // add code for hide unhide cart icon when medicine size zero 24-11-2020
            if (vm.selectedMedicines.value!!.size == 0) {
                vm.selectedMedicines.value!!.clear()
                // menu.findItem(R.id.action_cart).isVisible = false
                this.cartitem.isVisible = false

            }
        }


        vm.selectedMedCount.observe(this) {

            val menuItem = menu.findItem(R.id.action_cart)
            Log.d("Counter", "onCreateOptionsMenu: " + it)
            selecteMedsCount = it

            val actionView = menuItem.actionView
            val textCartItemCount = actionView.findViewById<View>(R.id.cart_badge) as TextView
            if (it == 0) {
                if (textCartItemCount.visibility != View.GONE) {
                    textCartItemCount.visibility = View.GONE
                    this.cartitem.isVisible = false
                }
            } else {
                textCartItemCount.text = Math.min(selecteMedsCount, 99).toString()
                if (textCartItemCount.visibility != View.VISIBLE) {
                    textCartItemCount.visibility = View.VISIBLE
                    this.cartitem.isVisible = true
                }
            }
        }

        return true
    }
    // click on toolbar backpress arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchItem = menu.findItem(R.id.app_bar_search)
        searchItem.isVisible = false


        val cartitem = menu.findItem(R.id.action_cart)
        cartitem.isVisible = false


        menu.findItem(R.id.action_home2).let {
            it.isVisible = true

            it.setOnMenuItemClickListener {

                    try {
                        startActivity(Intent(this, ResidentDetailsActivity::class.java).apply {
                            this.putExtra(ResidentDetailsActivity.KEY_ID, id)
                            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            finish()
                        })

                    }catch(e : Exception)
                    {
                        System.out.println("onclick home= "+e.message)
                    }

                true
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    // MenuItem.OnActionExpandListener
    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Search.setQuery("")
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1)
            supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
        super.onBackPressed()
    }

    fun goToPrescriptionFragment(){
        gotoFragment(prescriptionFragment, emptyStack = false, addToBackStack = true)
    }

    fun goToExtraDataFragment(){
        gotoFragment(orderExtraDataFragment, emptyStack = false, addToBackStack = true)
    }

    fun goToSupplierFragment() {
        gotoFragment(medicationSupplierFragment, emptyStack = false, addToBackStack = true)
    }

    fun goToMedicationsBasketFragment() {
        gotoFragment(medicationBasketFragment, emptyStack = false, addToBackStack = true)
    }

    fun goToMedicineListFragment() {
        gotoFragment(medicineListFragment, emptyStack = false, addToBackStack = true)
    }


    fun gotoOrderPreviewhome2(context: Context) {
        try {
            startActivity(Intent(context, ResidentDetailsActivity::class.java).apply {
                this.putExtra(ResidentDetailsActivity.KEY_ID, id)
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
            })

        }catch(e : Exception)
        {
            System.out.println("onclick home= "+e.message)
        }
    }

}