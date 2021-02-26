package reach52.marketplace.community.models.medication

import com.couchbase.lite.QueryEnumerator
import com.couchbase.lite.QueryRow
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.QueryResult
import reach52.marketplace.community.persistence.medication_mapper.PhysicianMapper

class PhysiciansResult(enumerator: QueryEnumerator) : QueryResult<Physician>(enumerator) {
    private val mapper = PhysicianMapper()

    override fun deserialize(row: QueryRow): Entity<Physician> =
            Entity(row.documentId, mapper.unmarshal(row.document.properties))
}
