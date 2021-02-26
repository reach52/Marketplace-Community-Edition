package reach52.marketplace.community.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class User(
        @JsonProperty("_id") val id: String,
        @JsonProperty("first_name") val firstName: String,
        @JsonProperty("last_name") val lastName: String,
        @JsonProperty("password") val passwordHash: String,
        @JsonProperty("username") val username: String,
        @JsonProperty(value = "country", required = false) val country: String?,
        @JsonProperty("roles") val roles: List<String>,
        @JsonProperty("marketPlace") val marketPlace : MarketPlace
) {
    val displayName: String = listOf(firstName, lastName)
            .filter(String::isNotBlank)
            .joinToString(" ")

    data class MarketPlace(
            @JsonProperty("catalogTags") val catalogTags: List<String>
    )
}