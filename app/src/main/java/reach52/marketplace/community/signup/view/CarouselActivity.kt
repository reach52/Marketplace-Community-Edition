package reach52.marketplace.community.signup.view

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.databinding.XCarouselItemBinding
import reach52.marketplace.community.extensions.utils.fromHtml
import reach52.marketplace.community.signup.entity.CarouselItem
import kotlinx.android.synthetic.main.activity_carousel.*

class CarouselActivity : BaseActivity() {

	var scrollPosition = 0
	val carouselItems = ArrayList<CarouselItem>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_carousel)
		hideStatusbar()

		val titles = resources.getStringArray(R.array.carousel_titles)
		val bodies = resources.getStringArray(R.array.carousel_bodies)

		carouselItems.add(CarouselItem(R.drawable.carousel_1, titles[0], bodies[0]))
		carouselItems.add(CarouselItem(R.drawable.carousel_2, titles[1], bodies[1]))
		carouselItems.add(CarouselItem(R.drawable.carousel_3, titles[2], bodies[2]))
		carouselItems.add(CarouselItem(R.drawable.carousel_4, titles[3], bodies[3]))
		carouselItems.add(CarouselItem(R.drawable.carousel_5, titles[4], bodies[4]))
		carouselItems.add(CarouselItem(R.drawable.carousel_6, titles[5], bodies[5]))

		setupCarouselList(carouselItems)

		next.setOnClickListener {
			startActivity(Intent(this, RegistrationActivity::class.java))
		}


		help_btn.setOnClickListener {
			startActivity(Intent(this, FAQActivity::class.java))
		}

	}

	private fun setupCarouselList(carouselItems: ArrayList<CarouselItem>) {
		carousel_list.adapter = CarouselListAdapter(carouselItems)
		val helper: SnapHelper = PagerSnapHelper()
		helper.attachToRecyclerView(carousel_list)

		carousel_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
				scrollPosition = layoutManager!!.findFirstCompletelyVisibleItemPosition()
				onScroll()
			}
		})
	}

	private fun onScroll() {

//		if (scrollPosition == carouselItems.size - 1) {
//			next_btn_text.setText(R.string.Continue)
//		} else {
//			next_btn_text.setText(R.string.next)
//		}

		updateDots()

	}

	private fun updateDots() {
		dot1.isEnabled = scrollPosition == 0
		dot2.isEnabled = scrollPosition == 1
		dot3.isEnabled = scrollPosition == 2
		dot4.isEnabled = scrollPosition == 3
		dot5.isEnabled = scrollPosition == 4
		dot6.isEnabled = scrollPosition == 5
	}

	inner class CarouselListAdapter constructor(val carouselItems: ArrayList<CarouselItem>) : RecyclerView.Adapter<CarouselItemVH>() {
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CarouselItemVH(DataBindingUtil.inflate(
				this@CarouselActivity.layoutInflater,
				R.layout.x_carousel_item, parent, false)
		)


		override fun onBindViewHolder(holder: CarouselItemVH, position: Int) {
			holder.setCarouselItem(carouselItems[position])
		}

		override fun getItemCount(): Int {
			return carouselItems.size
		}

	}

	inner class CarouselItemVH internal constructor(var b: XCarouselItemBinding) : RecyclerView.ViewHolder(b.root) {
		fun setCarouselItem(carouselItem: CarouselItem) {
			b.carouselItem = carouselItem
			b.subtext.text = fromHtml(carouselItem.subText, drawable = resources.getDrawable(R.drawable.ic_small_blue_check))
			if (!carouselItem.alignToCenter) {
				b.subtext.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
			}
		}

	}

}