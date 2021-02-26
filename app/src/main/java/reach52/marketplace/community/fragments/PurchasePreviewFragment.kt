package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsuranceBenefitAdapter
import reach52.marketplace.community.adapters.insurance.InsuredBeneficiaryAdapter
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.Insured
import reach52.marketplace.community.models.Purchases
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_purchase_preview.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalUnsignedTypes
class PurchasePreviewFragment : androidx.fragment.app.Fragment() {

    private val disposable = CompositeDisposable()

    companion object {
        private const val KEY_INSURED_ID = "insuredId"

        fun newInstance(insuredId: String): PurchasePreviewFragment {
            return PurchasePreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_INSURED_ID, insuredId)
                }
            }
        }
    }

    private val purchases by lazy {
        Purchases(DispergoDatabase.getInstance(context!!))
    }

    private val insuredId by lazy {
        arguments?.getString(KEY_INSURED_ID)!!
    }

    private val insuredIdBase58 by lazy {
        UUID.fromString(insuredId).base58String()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_purchase_preview, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = "Purchase Preview"

            this.isFocusableInTouchMode = true
            this.requestFocus()
            this.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
                    val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
                    val toggle = ActionBarDrawerToggle(
                            activity, drawer, toolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close)
                    toggle.syncState()
                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    return@OnKeyListener true
                }
                false
            })
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        insuranceBenefits.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        insuredBeneficiary.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)


        displayInsured(purchases.get(insuredId))

        disposable.add(backToHomeButton.clicks().subscribe({
            val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
            val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
            val toggle = ActionBarDrawerToggle(
                    activity, drawer, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            toggle.syncState()
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }, {
            throw NotImplementedError()
        }))
    }

    private fun displayInsured(insured: Insured) {
        val subject = insured.domainResource.subject
        val name = "${subject.firstName} ${subject.middleName} ${subject.lastName}"

        nameTextView.text = name
        contactText.text = insured.domainResource.contact
        emailText.text = insured.domainResource.emailAddress
        ageTextView.text = insured.domainResource.gender+", "+insured.domainResource.dateOfBirth.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        addressTextView.text = insured.domainResource.address.text
        civilStatusTextView.text = insured.domainResource.civilStatus
        insuranceTextView.text = insured.domainResource.plan[0].planOwnerName
        purchaseTxtView.text = insuredIdBase58
        planTxtView.text = insured.domainResource.plan[0].title
        agentTextView.text = insured.domainResource.plan[0].currentStatus.userDisplayName
        dateTextView.text = insured.domainResource.plan[0].currentStatus.statusDate.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        dateEndTextView.text = insured.domainResource.plan[0].effectiveDate.dateEnd.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        insuranceBenefits.adapter = InsuranceBenefitAdapter(ArrayList(insured.domainResource.plan[0].specificCost))
        insuredBeneficiary.adapter = InsuredBeneficiaryAdapter(ArrayList(insured.domainResource.coverage))
        priceTextView.text = insured.domainResource.plan[0].price.toPhilippinesCurrency()
    }
}