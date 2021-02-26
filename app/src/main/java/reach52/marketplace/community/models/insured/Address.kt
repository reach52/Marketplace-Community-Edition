package reach52.marketplace.community.models.insured

import arrow.core.Option
import arrow.core.Some
import reach52.marketplace.community.extensions.catOptions

data class Address(
        val city: Option<String>,
        val country: Option<String>,
        val district: Option<String>,
        val line: List<String>,
        val postalCode: Option<String>,
        val state: Option<String>) {
    private val l = line.filterNot { it.isBlank() }.joinToString(" ")
    private val a = listOf(Some(l), district, city, state, country).catOptions().joinToString()
    val text: String = listOf(Some(a), postalCode).catOptions().joinToString(" ")
}


