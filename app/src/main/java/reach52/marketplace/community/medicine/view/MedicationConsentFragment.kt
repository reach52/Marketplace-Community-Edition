package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.checkedChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.CountryCode
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.fromHtml
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.consent_fragment.view.agreeCheckBox
import kotlinx.android.synthetic.main.consent_fragment.view.consentButton
import kotlinx.android.synthetic.main.fragment_insurance_consent.view.*

class MedicationConsentFragment : Fragment() {

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // TODO remove insurance constent with a general constent
        return inflater.inflate(R.layout.fragment_insurance_consent, container, false).apply {
//            MedicineRepo.getAllMedicines(context!!, "MEDEXP").subscribe()
            if (CountryManager.getCountryCode() == CountryCode.IND) {
                consent_text?.text = fromHtml(getString(R.string.india_consent))
            } else {
                consent_text?.text = fromHtml(getString(R.string.india_consent))
            }
//            consentWebView.loadDataWithBaseURL(null, getString(R.string.consent_text), "text/html", "utf-8", null)


            disposables.add(agreeCheckBox.checkedChanges().subscribe({
                consentButton.isEnabled = it
            }, {
                Log.w("ConsentFragment", "agreeCheckBox.checkedChanges", it)
                throw NotImplementedError()
            }))

            disposables.add(consentButton.clicks().subscribe({
                (activity as MedicationPurchaseActivity).goToExtraDataFragment()

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


    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        (activity as MedicationPurchaseActivity).supportActionBar?.title = getString(R.string.consent_insurance)

    }
}
