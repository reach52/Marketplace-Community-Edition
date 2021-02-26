package reach52.marketplace.community.insurance.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentPremiumBinding
import reach52.marketplace.community.insurance.adapter.BreakdownMembersAdapter
import reach52.marketplace.community.insurance.viewmodel.InsurancePurchaseViewModel
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel

class PremiumFragment : Fragment() {

	private lateinit var vm: InsurancePurchaseViewModel
	private lateinit var ctx: Context
	lateinit var vmr: ResidentViewModel
	private lateinit var primaryAdapter: BreakdownMembersAdapter
	private var extraAdapter: BreakdownMembersAdapter? = null
	private lateinit var b: FragmentPremiumBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		ctx = context
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.save).let {
			it.isVisible = false
		}
		menu.findItem(R.id.home_action).let {
			it.isVisible = true

			it.setOnMenuItemClickListener {
				try {

					(activity as InsurancePurchaseActivity).gotoOrderPreviewhome(context!!)
				} catch (e: java.lang.Exception) {
					System.out.println("onclick home= " + e.message)
				}
				true
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		b = FragmentPremiumBinding.inflate(inflater, container, false)

		val activity = activity as InsurancePurchaseActivity
		vm = ViewModelProvider(activity)[InsurancePurchaseViewModel::class.java]
		vmr = ViewModelProvider(activity)[ResidentViewModel::class.java]
		val periods = vm.selectedConstruct.premium.keys.toList()
		b.periodSpinner.adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, periods)
		b.periodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
				vm.premiumPeriod.value = parent.selectedItem as String
			}

			override fun onNothingSelected(parent: AdapterView<*>) {
				vm.premiumPeriod.value = parent.getItemAtPosition(0) as String
			}

		}
		b.periodSpinner.post {
			b.periodSpinner.setSelection(0)
		}
		val tax = vm.selectedInsuranceProduct.tax
		b.taxPercentage.text = "${tax.percentage}% ${tax.type}"

		vm.premiumPeriod.observe(activity, Observer { period ->

			val currency = vm.selectedInsuranceProduct.isoCurrency

			if (!::primaryAdapter.isInitialized) {
				primaryAdapter = BreakdownMembersAdapter(ctx, vm.breakdownPrimaryMembers, currency, period)
			} else {
				primaryAdapter.changePeriod(period)
			}
			b.primaryMembers.adapter = primaryAdapter
			b.primarySubtotal.text = "${vm.selectedConstruct.premium[period]!!.amount} ${currency.toUpperCase()}"

			if (vm.breakdownExtraMembers.isEmpty()) {
				b.extraGroup.visibility = View.GONE
			} else {
				if (extraAdapter == null) {
					extraAdapter = BreakdownMembersAdapter(ctx, vm.breakdownExtraMembers, currency, period)
				} else {
					extraAdapter!!.changePeriod(period)
				}
				b.extraMembers.adapter = extraAdapter

				val extraPreimum = vm.calculateExtraPremium(period)
				b.extraSubtotal.text = "$extraPreimum ${currency.toUpperCase()}"

			}
			val total = vm.calculateTotalPremium(period)
			val final = vm.calculateTotalPremiumWithTax()

			b.totalAmount.text = "$total ${currency.toUpperCase()}"
			b.finalPremium.text = "$final ${currency.toUpperCase()}"

		})

		b.proceedBtn.setOnClickListener {
			activity.gotoBeneficiariesFragment()
		}


		return b.root
	}


	/*//click on home button 16-12-2020
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.getItemId()) {
			R.id.home_action -> {

				try {
					activity?.startActivity(Intent(activity, ResidentDetailsActivity::class.java).apply {
						this.putExtra(ResidentDetailsActivity.KEY_ID, vmr.residentId)
					})
				}catch(e : java.lang.Exception)
				{
					System.out.println("onclick home= "+vmr.residentId+" "+e.message)
				}


				return true
			}
		}
		return super.onOptionsItemSelected(item)
	}*/

	override fun onResume() {
		super.onResume()
		(activity as InsurancePurchaseActivity).supportActionBar?.title = getString(R.string.premium)
	}
}