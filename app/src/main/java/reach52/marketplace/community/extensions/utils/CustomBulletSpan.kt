package reach52.marketplace.community.extensions.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan

/**
 * Copy of [android.text.style.BulletSpan] from android SDK 28 with removed internal code
 */
class CustomBulletSpan(
		val bulletRadius: Int = STANDARD_BULLET_RADIUS,
		val gapWidth: Int = STANDARD_GAP_WIDTH,
		val color: Int = STANDARD_COLOR,
		val drawable: Drawable? = null
) : LeadingMarginSpan {

	companion object {
		// Bullet is slightly bigger to avoid aliasing artifacts on mdpi devices.
		private const val STANDARD_BULLET_RADIUS = 4
		private const val STANDARD_GAP_WIDTH = 2
		private const val STANDARD_COLOR = 0
	}

	private var mBulletPath: Path? = null

	override fun getLeadingMargin(first: Boolean): Int {
		if (drawable != null) {
			return 2 * bulletRadius + ((gapWidth/2) + drawable.intrinsicWidth)
		} else {
			return 2 * bulletRadius + gapWidth
		}
	}

	override fun drawLeadingMargin(
			canvas: Canvas, paint: Paint, x: Int, dir: Int,
			top: Int, baseline: Int, bottom: Int,
			text: CharSequence, start: Int, end: Int,
			first: Boolean,
			layout: Layout?
	) {
		val bottom = bottom
		if ((text as Spanned).getSpanStart(this) == start) {
			val style = paint.style
			paint.style = Paint.Style.FILL

			val yPosition = if (layout != null) {
				val line = layout.getLineForOffset(start)
				layout.getLineBaseline(line).toFloat() - bulletRadius * 2f
			} else {
				(top + bottom) / 2f
			}

			val xPosition = (x + dir * bulletRadius).toFloat()

			if (drawable != null) {
				val dY = (top + 8)
				drawable.setBounds(
						0,
						dY,
						(drawable.intrinsicWidth),
						(dY + drawable.intrinsicHeight))
				drawable.draw(canvas)
			} else {
				if (canvas.isHardwareAccelerated) {
					if (mBulletPath == null) {
						mBulletPath = Path()
						mBulletPath!!.addCircle(0.0f, 0.0f, bulletRadius.toFloat(), Direction.CW)
					}

					canvas.save()
					canvas.translate(xPosition, yPosition)
					canvas.drawPath(mBulletPath!!, paint)
					canvas.restore()
				} else {
					canvas.drawCircle(xPosition, yPosition, bulletRadius.toFloat(), paint)
				}
			}


			paint.style = style
		}
	}
}