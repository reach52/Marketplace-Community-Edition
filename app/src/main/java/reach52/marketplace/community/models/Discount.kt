package reach52.marketplace.community.models

import kotlin.math.round

data class Discount(val code: String, val discount: Double, val vat: Double) {
    fun vatExempt(amount: Double): Double = round((amount / (1.00 + vat) * 100)) / 100.0
    fun discountAmount(amount: Double): Double = round(vatExempt(amount * discount) * 100) / 100.0
    fun fee(amount: Double): Double = round( amount + 24.00)
    fun total(amount: Double): Double = round(((vatExempt(amount) - discountAmount(amount)) * 100)) / 100.0
}