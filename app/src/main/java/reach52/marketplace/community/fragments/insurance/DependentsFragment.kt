package reach52.marketplace.community.fragments.insurance

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.DependentsAdapter
import reach52.marketplace.community.extensions.utils.createDatePicker
import reach52.marketplace.community.models.Dependent
import reach52.marketplace.community.persistence.DependentManager
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import kotlinx.android.synthetic.main.fragment_dependents.*
import kotlinx.android.synthetic.main.fragment_dependents.view.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class DependentsFragment : Fragment() {

	private lateinit var ctx: Context

	private lateinit var adapter: DependentsAdapter

	private lateinit var newDependentDialog: AlertDialog
	private lateinit var dialogLayout: View
	private var dob: String = ""

	private val insuranceViewModel by lazy {
		activity?.run {
			ViewModelProvider(this)[InsuranceViewModel::class.java]
		} ?: throw Exception("Invalid Activity")
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		this.ctx = context
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		super.onPrepareOptionsMenu(menu)
		menu.findItem(R.id.app_bar_search).isVisible = false
		menu.findItem(R.id.dependents_save).let {
			it.isVisible = true
			it.setOnMenuItemClickListener {

				if (insuranceViewModel.dependents.isNotEmpty()) {

					parentFragmentManager.beginTransaction().apply {
						replace(R.id.fragment_container, InsurancePurchasePreviewFragment())
						addToBackStack(null)
						commit()
					}
				} else {
					Toast.makeText(context, getString(R.string.add_dependents_to_countinue), Toast.LENGTH_SHORT).show()
				}
				true
			}
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val view = inflater.inflate(R.layout.fragment_dependents, container, false).apply {
			(activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.dependents)
		}
		adapter = DependentsAdapter(ctx,
				insuranceViewModel.dependents,
				listChangeListener = object : DependentsAdapter.ListChangeListener {
					override fun onDependentAdded(listSize: Int) {

						if (listSize == 1) {
							dependents_message.visibility = View.GONE
						}

						if (listSize >= 3) {
							view.add_dependents_btn.visibility = View.GONE
						} else {
							view.add_dependents_btn.visibility = View.VISIBLE
						}

					}

					override fun onDependentRemoved(listSize: Int) {

						if (listSize == 0) {
							view.dependents_message.visibility = View.VISIBLE
						}
						view.add_dependents_btn.visibility = View.VISIBLE

					}

				})

		view.dependents_message.visibility = if (insuranceViewModel.dependents.isEmpty()) View.VISIBLE else View.GONE
		view.add_dependents_btn.visibility = if (insuranceViewModel.dependents.size < 3) View.VISIBLE else View.GONE


		dialogLayout = LayoutInflater.from(ctx).inflate(R.layout.layout_new_dependent, null)
		val dateFormat = SimpleDateFormat("yyyy-MM-dd")
		dialogLayout.findViewById<EditText>(R.id.dependent_dob).setOnClickListener { view ->

			val datePicker = createDatePicker(ctx,
					DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
						run {
							val cal = Calendar.getInstance()
							cal.set(year, month, dayOfMonth)
							dob = dateFormat.format(cal.time)
							(view as EditText).setText("$dayOfMonth/$month/$year")
						}
					}
			)
			datePicker.show()

		}
		newDependentDialog = AlertDialog.Builder(ctx)
				.setView(dialogLayout)
				.setCancelable(false)
				.setNegativeButton(getString(R.string.cancel), null)
				.setPositiveButton(getString(R.string.add), null)
				.create()

		val addClickListener = View.OnClickListener {

			if (dob.isEmpty()) {
				Toast.makeText(ctx, getString(R.string.date_cannot_be_empty), Toast.LENGTH_SHORT).show()
				return@OnClickListener
			}

			val name = dialogLayout.findViewById<EditText>(R.id.dependent_name).text.toString()
			val dob = ZonedDateTime.parse(dob + "T00:00:00-05:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
			val gender = dialogLayout.findViewById<Spinner>(R.id.dependent_gender).selectedItem.toString()
			val relation = dialogLayout.findViewById<Spinner>(R.id.dependent_relation).selectedItem.toString()

			val dependent = Dependent(name, dob, gender, relation)

			DependentManager.validateDependent(dependent).subscribe(
					{
						if (it) {
							adapter.addDependent(dependent)
							newDependentDialog.dismiss()
						}
					},
					{
						when (it) {
							is DependentManager.EmptyNameException -> {
								Toast.makeText(ctx, getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show()
							}
							is DependentManager.AgeTooLowException -> {
								Toast.makeText(ctx, getString(R.string.age_too_low), Toast.LENGTH_SHORT).show()
							}
							else -> {
								Toast.makeText(ctx, it.message, Toast.LENGTH_SHORT).show()
							}
						}
					}
			)
		}

		newDependentDialog.setOnShowListener {
			newDependentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(addClickListener)
		}

		view.dependents_list.adapter = adapter
		view.dependents_list.setHasFixedSize(true)

		view.add_dependents_btn.setOnClickListener {

			clearDialog()
			newDependentDialog.show()

		}

		return view
	}

	private fun clearDialog() {
		dialogLayout.findViewById<EditText>(R.id.dependent_name).text.clear()
		dialogLayout.findViewById<EditText>(R.id.dependent_name).clearFocus()
		dialogLayout.findViewById<EditText>(R.id.dependent_dob).text.clear()
		dob = ""

	}


}