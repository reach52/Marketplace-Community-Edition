package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.viewmodels.OrderViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health.view.*

class ConsumerHealthFragment : androidx.fragment.app.Fragment()  {
    private lateinit var mModel: Option<OrderViewModel>
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_consumer_health, container, false).apply {
            disposables.add(viewProductsAndServicesButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConsumerHealthConsentFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                Log.w("ResidentDetailsFragment", "newMedicationOrderButton", it)
                throw NotImplementedError()
            }))

        disposables.add(viewSavedProductButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConsumerHealthDiscountSelectionSavedFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                Log.w("ResidentDetailsFragment", "newMedicationOrderButton", it)
                throw NotImplementedError()
            }))
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
