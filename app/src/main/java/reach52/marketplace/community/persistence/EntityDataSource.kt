package reach52.marketplace.community.persistence

import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.couchbase.lite.QueryEnumerator
import reach52.marketplace.community.models.Entity
import kotlin.math.min

class EntityDataSource<T>(private val enumerator: QueryEnumerator, private val unmarshaler: Unmarshaler<T>) : PositionalDataSource<Entity<T>>() {
    companion object {
        val PAGED_LIST_CONFIG: PagedList.Config = PagedList.Config.Builder()
                .setPrefetchDistance(16)
                .setInitialLoadSizeHint(16)
                .setPageSize(16)
                .build()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Entity<T>>) {
        val count = enumerator.count

        if (count > 0) {
            val start = min(params.requestedStartPosition, count - 1)
            val end = min(params.requestedLoadSize + start, count) - 1
            callback.onResult(getRows(start, end), start, count)
        } else
            callback.onResult(listOf(), 0, count)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Entity<T>>) {
        callback.onResult(getRows(params.startPosition, params.startPosition + params.loadSize - 1))
    }

    private fun getRows(start: Int, end: Int) = (start..end).map {
        enumerator.getRow(it)
    }.map {
        Entity(it.documentId, unmarshaler.unmarshal(it.document.properties))
    }
}
