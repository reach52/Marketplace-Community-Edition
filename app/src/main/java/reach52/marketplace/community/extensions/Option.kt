package reach52.marketplace.community.extensions

import arrow.core.None
import arrow.core.Option
import arrow.core.Some

fun <T> List<Option<T>>.catOptions(): List<T> = this.fold(mutableListOf(), { acc, x ->
    when (x) {
        is Some -> {
            acc.add(x.t)
            acc
        }
        is None -> acc
    }

})
