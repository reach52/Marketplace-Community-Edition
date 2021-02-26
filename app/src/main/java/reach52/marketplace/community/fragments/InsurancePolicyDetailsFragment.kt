package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_policy_details.view.*

class InsurancePolicyDetailsFragment : Fragment() {

    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_policy_details, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.insurance_policy_details)

            insurancePolicy.loadDataWithBaseURL(null, getString(R.string.policy_text), "text/html", "utf-8", null)

            disposable.add(buttonGoBack.clicks().subscribe {
                parentFragmentManager.popBackStack()
            })

        }
    }
}