package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentMedicineSupplierBinding
import reach52.marketplace.community.medicine.adapter.MedicationSupplierAdapter
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.medicine.viewmodel.MedicationOrderViewModel
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import reach52.marketplace.community.medicine.viewmodel.MedicineViewModel
import kotlinx.android.synthetic.main.activity_faq.*


class MedicationSupplierFragment : Fragment(), MedicationSupplierAdapter.Onclick{
    private lateinit var vm: MedicationOrderViewModel
    private lateinit var vmPur: MedicationPurchaseViewModel
    private lateinit var vmMed: MedicineViewModel
    var listSizee: ArrayList<Supplier> = ArrayList<Supplier>()
    var supplierlistt: ArrayList<Supplier> = ArrayList<Supplier>()
    //var suppliers = MutableLiveData<ArrayList<Supplier>>()
    lateinit var adapter:MedicationSupplierAdapter;
    lateinit var b : FragmentMedicineSupplierBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val activity = activity as MedicationPurchaseActivity
        vm = ViewModelProvider(activity)[MedicationOrderViewModel::class.java]
        vmPur = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]
        vmMed = ViewModelProvider(activity)[MedicineViewModel::class.java]

        b = FragmentMedicineSupplierBinding.inflate(inflater, container, false)
        vm.suppliers.observe(activity, Observer { list ->

            adapter = MedicationSupplierAdapter(context!!, list, this)
            b.suppliersListRecyclerView.adapter = adapter;
        })

        vm.loadSuppliers(context!!)




// add code on click button get all selected supplier datd
        b.selectSupplier.setOnClickListener(View.OnClickListener {

            var counter = 0

            // clear suplier CodeList         24-11-2020

            vmMed.suplCodeList.clear()
            vmPur.selectedmultiSupplier.clear()

            for (i in vm.suppliers.value!!) {
                if (i.status == 1) {
                    // add selected supplier data
                    vmPur.selectedmultiSupplier.add(i)

                    // add selected supplier code 24-11-2020
                    vmMed.suplCodeList.add(i.supplierCode)
                    counter = +1
                }
            }
            if (counter == 0) {
                // b.selectSupplier.isClickable = false
                Toast.makeText(getActivity(), "Please Select Suppliers", Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                (activity as MedicationPurchaseActivity).goToMedicationsBasketFragment()
            }
            // coppied the selected supplier list


            /*  vm.suppliers.observe(activity, Observer {

                System.out.println("suppliers "+it)
            })

            System.out.println("list= 2 "+listSizee)*/

            //saveArrayList( supplierlistt, "seletcSupplierId");
            //vmPur.selectedmultiSupplier=supplierlistt


        })


        b.selectAllCheckBox.setOnClickListener({
            if (b.selectAllCheckBox.isChecked)
            {
                adapter.itemSelected(true)
            }
            else
            {
                adapter.itemSelected(false)
            }
        })
        return b.root



    }

    // set multiselected supplier id into shared preferences
    private fun saveArrayList(supplierlistt: ArrayList<Supplier>, key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(supplierlistt)
        editor.putString(key, json)
        editor.apply()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_cart).isVisible = false
    }

    override fun onResume() {
        super.onResume()
        (activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.choose_supplier)
    }


    override fun onItemChecked() {

        Log.d("onGETCHECK", "ongetcheck: called ongetCheck")

        if (vm.suppliers.value!!.any { it.status ==0})
        {
            b.selectAllCheckBox.isChecked = false;
        }
        else
        {
            b.selectAllCheckBox.isChecked= true;
        }
    }

}
