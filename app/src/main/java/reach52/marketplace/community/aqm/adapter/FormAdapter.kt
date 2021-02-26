package reach52.marketplace.community.aqm.adapter

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.aqm.entity.Item
import reach52.marketplace.community.aqm.entity.Item.Companion.MULTI_SELECT_ITEM
import reach52.marketplace.community.aqm.entity.Item.Companion.SINGLE_SELECT_ITEM
import reach52.marketplace.community.aqm.entity.Item.Companion.TEXT_ITEM
import reach52.marketplace.community.aqm.entity.Item.Option
import reach52.marketplace.community.aqm.entity.MultiSelectItem
import reach52.marketplace.community.aqm.entity.SingleSelectItem
import reach52.marketplace.community.aqm.entity.TextItem
import reach52.marketplace.community.databinding.XSingleSelectItemBinding
import reach52.marketplace.community.databinding.XTextItemBinding


class FormAdapter(
		val context: Context,
		val formItems: List<Item>,
		val optionSelectListener: OptionSelectListener? = null
) : RecyclerView.Adapter<FormAdapter.ItemVH>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {

		return when (viewType) {
			TEXT_ITEM -> TextItemVH(
					DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_text_item, parent, false)
			)
			else -> SingleSelectItemVH(
					DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_single_select_item, parent, false)
			)
		}

	}

	override fun getItemCount(): Int {
		return formItems.size
	}

	override fun onBindViewHolder(holder: ItemVH, position: Int) {
		val item = formItems[position]
		holder.setItem(item)
	}

	override fun getItemViewType(position: Int): Int {
		val item = formItems[position]

		return when (item) {
			is SingleSelectItem -> SINGLE_SELECT_ITEM
			is MultiSelectItem -> MULTI_SELECT_ITEM
			is TextItem -> TEXT_ITEM
			else -> -1
		}

	}

	abstract class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
		abstract fun setItem(formItem: Item)
	}

	inner class TextItemVH(val b: XTextItemBinding) : ItemVH(b.root) {
		override fun setItem(formItem: Item) {

			val item = formItem as TextItem
			b.question.text = item.question

			when (item.inputType) {

				TextItem.EMAIL -> {
					b.answer.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
				}

				TextItem.NAME -> {
					b.answer.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
				}

				TextItem.NUMBER -> {
					b.answer.inputType = InputType.TYPE_CLASS_NUMBER
				}

				TextItem.PHONE -> {
					b.answer.inputType = InputType.TYPE_CLASS_PHONE
				}

				TextItem.ADDRESS -> {
					b.answer.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
				}

				else -> {
					b.answer.inputType = InputType.TYPE_CLASS_TEXT
				}

			}

			b.answer.textChanges().subscribe {
				item.value = it.toString()
			}

		}

	}

	inner class SingleSelectItemVH(val b: XSingleSelectItemBinding) : ItemVH(b.root) {
		override fun setItem(formItem: Item) {

			val item = formItem as SingleSelectItem

			b.question.text = item.question

			val optionNames = item.options.map(Option::text)

			val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, optionNames)
			b.spinner.adapter = adapter
			b.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onNothingSelected(parent: AdapterView<*>?) {
				}

				override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
					val selectedOption = item.options[position]
					item.value = selectedOption
					optionSelectListener?.onOptionSelect(item, selectedOption)
				}

			}

		}

	}

	interface OptionSelectListener {
		fun onOptionSelect(item: SingleSelectItem, option: Option)
	}

}