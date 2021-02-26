package reach52.marketplace.community.models.insurance

import com.couchbase.lite.Database
import reach52.marketplace.community.persistence.insurer_mapper.InsurerDocumentMapper

class Insurers(private val database: Database) {

    fun get(insurerId:  String): InsurerDocument = InsurerDocumentMapper().unmarshal(database.getDocument(insurerId).properties)

}