package reach52.marketplace.community.persistence

interface Marshaler<T> {
    fun marshal(t: T): Map<String, Any>
}