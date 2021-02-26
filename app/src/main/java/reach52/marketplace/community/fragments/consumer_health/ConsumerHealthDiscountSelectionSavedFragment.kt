package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.itemSelections
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.viewmodels.OrderViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_discount_selection_saved.*
import kotlinx.android.synthetic.main.fragment_prescription.capturedImageView
import kotlinx.android.synthetic.main.fragment_prescription.prescriptionNumberEditText
import kotlinx.android.synthetic.main.fragment_prescription.takePhotoButton
import kotlinx.android.synthetic.main.order_extra_data_fragment.view.*

class ConsumerHealthDiscountSelectionSavedFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private lateinit var discounts: Map<String, Option<Discount>>
    private lateinit var model: Option<OrderViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        model = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java].also { orderViewModel ->
                discounts = orderViewModel.discounts.map { discount ->
                    Pair<String, Option<Discount>>(discount.code, Some(discount))
                }.startWith(Pair<String, Option<Discount>>(getString(R.string.discount_optional), None)).toList()
                        .blockingGet().toMap()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Discount selection"
        return inflater.inflate(R.layout.fragment_consumer_health_discount_selection_saved,
                container, false).apply{
            discountCodeSpinner.adapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    discounts.keys.toTypedArray()
            )

            disposables.add(discountCodeSpinner.itemSelections().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.selectedDiscount =
                            discounts.getValue(discountCodeSpinner.getItemAtPosition(it) as String)
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(discountIdNumberTextInputEditText.textChanges().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.discountIdNumber = if (it.isNullOrBlank()) {
                        None
                    } else {
                        Some(it.toString())
                    }
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(prescriptionNumberEditText.textChanges().subscribe({
                model.map { orderViewModel ->
                    orderViewModel.hcpNumber = if (it.isNullOrBlank()) {
                        None
                    } else {
                        Some(it.toString())
                    }
                }
            }, {
                throw NotImplementedError()
            }))

            disposables.add(orderMedicineButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ConsumerHealthSavedFragment())
                        .addToBackStack(null)
                        .commit()

            }, {
                throw NotImplementedError()
            }))
            capturedImageView.visibility = View.INVISIBLE
            physicianLicenseSpinner.visibility = View.INVISIBLE
            takePhotoButton.visibility = View.INVISIBLE
            orderMedicineButton.text = getString(R.string.order_otc)
            prescriptionNumberEditText.hint = "HCP Number"
            prescriptionNumberEditText.visibility = View.INVISIBLE
        }
    }

}
