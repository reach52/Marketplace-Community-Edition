package reach52.marketplace.community.models

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class Search {
    companion object {
        private val emitter: PublishSubject<String> = PublishSubject.create()

        val query: Observable<String> = emitter

        fun setQuery(query: String) {
            emitter.onNext(query)
        }
    }
}
