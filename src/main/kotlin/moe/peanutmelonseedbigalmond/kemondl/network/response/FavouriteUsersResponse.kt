package moe.peanutmelonseedbigalmond.kemondl.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class FavouriteUsersResponse(
    @SerializedName("faved_seq") val favouriteSeq: Long,
    @SerializedName("id") val creatorId: String,
    @SerializedName("indexed") val indexedTime: Date,
    @SerializedName("name") val creatorName: String,
    @SerializedName("service") val site: String,
    @SerializedName("updated") val updatedDate: Date,
)