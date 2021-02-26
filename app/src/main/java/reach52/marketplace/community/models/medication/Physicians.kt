package reach52.marketplace.community.models.medication

import com.couchbase.lite.Database
import com.couchbase.lite.View
import reach52.marketplace.community.persistence.Utils
import reach52.marketplace.community.persistence.medication_mapper.PhysicianMapper

class Physicians(private val database: Database) {
    companion object {
        private const val VERSION_PHYSICIANS = "2"
        private const val VIEW_PHYSICIANS = "physicians"

        @Volatile
        private var PHYSICIANS: View? = null

        private fun getPhysician(document: Map<String, Any>) =
                Utils.getModel(PhysicianMapper.TYPE_PHYSICIAN, PhysicianMapper(), document)
    }

    private fun view(): View = PHYSICIANS
            ?: synchronized(this) {
        PHYSICIANS
                ?: database.getView(VIEW_PHYSICIANS).apply {
            setMap({ document, emitter ->
                getPhysician(document).map {
                    emitter.emit(it.licenseNumber, null)
                }
            }, VERSION_PHYSICIANS)
            PHYSICIANS = this
        }
    }

    fun all(): PhysiciansResult {
        return PhysiciansResult(view().createQuery().run())
    }
}