package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XInsuranceProductItemBinding


import reach52.marketplace.community.insurance.entity.InsuranceProduct

class InsuranceProductListAdapter (
		val context: Context,
		val products: ArrayList<InsuranceProduct>,
		val onProductSelect : (product: InsuranceProduct) -> Unit = {}
) : RecyclerView.Adapter<InsuranceProductListAdapter.ProductViewHolder>(){

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

		return ProductViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_insurance_product_item, parent, false)
		)

	}

	override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

		val product = products[position]
		holder.setProduct(product)
		holder.itemView.setOnClickListener {
			onProductSelect(product)
		}

	}

	override fun getItemCount() = products.size

	inner class ProductViewHolder(val b: XInsuranceProductItemBinding) : RecyclerView.ViewHolder(b.root){
		fun setProduct(product: InsuranceProduct){
			b.product = product
		}
	}

}