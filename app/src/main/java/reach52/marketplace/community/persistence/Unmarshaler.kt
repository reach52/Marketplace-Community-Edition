package reach52.marketplace.community.persistence

interface Unmarshaler<T> {
    fun unmarshal(properties: Map<String, Any>): T
}