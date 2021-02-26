package reach52.marketplace.community.insurance.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.checkedChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.CountryCode
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.fromHtml
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_consent.view.*


class InsuranceConsentFragment : Fragment() {

	private val disposables = CompositeDisposable()
	lateinit var vm: ResidentViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_insurance_consent, container, false).apply {
			vm = ViewModelProvider(activity as InsurancePurchaseActivity)[ResidentViewModel::class.java]
			if (CountryManager.getCountryCode() == CountryCode.IND) {
				consent_text.text = fromHtml(getString(R.string.india_consent))
			} else {
				consent_text.text = fromHtml(getString(R.string.consent_text))
			}

			disposables.add(agreeCheckBox.checkedChanges().subscribe({
				consentButton.isEnabled = it
			}, {
				Log.w("ConsentFragment", "agreeCheckBox.checkedChanges", it)
				throw NotImplementedError()
			}))

			disposables.add(consentButton.clicks().subscribe({
				(activity as InsurancePurchaseActivity).gotoInsurers()

			}, {
				Log.w("ConsentFragment", "consentButton", it)
				throw NotImplementedError()
			}))

		}



	}
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}


/*	override fun onCreateOptionsMenu(menu: Menu): Boolean {

		menuInflater.inflate(R.menu.insurance_purchase, menu)

			menu.findItem(R.id.home_action).actionView.setOnClickListener {
                try {
                    System.out.println("rclick1= ")
                }catch (e: java.lang.Exception)
                {
                    e.message
                }
            }
		return true
	}*/


	//click on home button
	/*override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

			*//*	try {
					context!!.startActivity(Intent(context, ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, vm.residentId)
					})
				}catch(e : Exception)
				{
					System.out.println("onclick home= "+vm.residentId+" "+e.message)
				}
*//*
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				}catch(e : java.lang.Exception)
				{
					System.out.println("onclick home= "+vm.residentId+" "+e.message)
				}
				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}*/


	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				}catch(e : java.lang.Exception)
				{
					System.out.println("onclick home= "+e.message)
				}
				true
			}
		}

	/*	val homeitem = menu.findItem(R.id.home_action)
		homeitem.isVisible = true*/
	}
	/*override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		val homeitem = menu.findItem(R.id.home_action)
		homeitem.isVisible = true
	}
*/




	override fun onDestroyView() {
		disposables.clear()
		super.onDestroyView()
	}

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.consent_insurance)
	}

}
