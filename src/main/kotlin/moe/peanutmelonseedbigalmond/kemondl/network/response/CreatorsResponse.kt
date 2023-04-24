package moe.peanutmelonseedbigalmond.kemondl.network.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class CreatorsResponse(
    @SerializedName("favorite") val favoriteCount: Int,
    val id: String,
    @SerializedName("indexed") val indexedDate: Date,
    val name: String,
    val service: String,
    @SerializedName("updated") val updatedDate: Date,
)