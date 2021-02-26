package reach52.marketplace.community.persistence

import androidx.paging.DataSource
import com.couchbase.lite.QueryEnumerator
import reach52.marketplace.community.models.Entity

class EntityDataSourceFactory<T>(var fnEnumerator: () -> QueryEnumerator,
                                 private val unmarshaler: Unmarshaler<T>) : DataSource.Factory<Int,
        Entity<T>>() {
    override fun create(): DataSource<Int, Entity<T>> {
        return EntityDataSource(fnEnumerator(), unmarshaler)
    }
}
