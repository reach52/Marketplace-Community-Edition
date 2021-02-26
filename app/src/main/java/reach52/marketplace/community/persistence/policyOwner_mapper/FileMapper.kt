package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.File
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class FileMapper: Marshaler<File>, Unmarshaler<File> {
    companion object{
        const val KEY_CONTENT_TYPE = "contentType"
        const val KEY_DIGEST = "digest"
        const val KEY_LENGTH = "length"
        const val KEY_REPOS = "repos"
        const val KEY_STUB = "stub"
    }

    override fun marshal(t: File): Map<String, Any> {
        return mapOf(
                KEY_CONTENT_TYPE to t.contentType,
                KEY_DIGEST to t.digest,
                KEY_LENGTH to t.length,
                KEY_REPOS to t.repos,
                KEY_STUB to t.stub
        )
    }

    override fun unmarshal(properties: Map<String, Any>): File {
        val contentType = properties[KEY_CONTENT_TYPE] as String
        val digest = properties[KEY_DIGEST] as String
        val length = (properties[KEY_LENGTH] as Number).toDouble()
        val repos = (properties[KEY_REPOS] as Number).toInt()
        val stub = properties[KEY_STUB] as Boolean

        return File(
                contentType = contentType,
                digest = digest,
                length = length,
                repos = repos,
                stub = stub
        )
    }
}