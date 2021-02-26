package reach52.marketplace.community.fragments.medication

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
import kotlinx.android.synthetic.main.fragment_medication_list.view.*

@ExperimentalUnsignedTypes
class MedicationFragment : androidx.fragment.app.Fragment() {

    private val disposables = CompositeDisposable()

    private lateinit var model: Option<OrderViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        model = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_medication_list, container, false).apply {
            disposables.add(newMedicationOrderButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConsentFragment())
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
