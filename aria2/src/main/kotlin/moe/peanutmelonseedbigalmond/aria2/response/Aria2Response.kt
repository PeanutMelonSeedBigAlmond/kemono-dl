package moe.peanutmelonseedbigalmond.aria2.response

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

data class Aria2Response(
    val id: String? = null,
    val method: String? = null,
    val result: JsonElement, //nullable
    val params: JsonArray? = null
)
