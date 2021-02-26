package reach52.marketplace.community.models

import com.couchbase.lite.QueryEnumerator
import com.couchbase.lite.QueryRow

abstract class QueryResult<T>(private val enumerator: QueryEnumerator) {
    val count: Int = enumerator.count

    fun get(position: Int): Entity<T> = deserialize(enumerator.getRow(position))

    fun toList(): List<Entity<T>> = enumerator.map { deserialize(it) }

    abstract fun deserialize(row: QueryRow): Entity<T>
}