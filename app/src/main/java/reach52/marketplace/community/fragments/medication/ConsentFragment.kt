package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.checkedChanges
import reach52.marketplace.community.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.consent_fragment.view.*

@ExperimentalUnsignedTypes
class ConsentFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consent_fragment, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.consent)
            consentWebView.loadDataWithBaseURL(null, getString(R.string.consent_text), "text/html", "utf-8", null)
            disposables.add(agreeCheckBox.checkedChanges().subscribe({
                consentButton.isEnabled = it
            }, {
                Log.w("ConsentFragment", "agreeCheckBox.checkedChanges", it)
                throw NotImplementedError()
            }))

            disposables.add(consentButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, OrderFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                Log.w("ConsentFragment", "consentButton", it)
                throw NotImplementedError()
            }))
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}
