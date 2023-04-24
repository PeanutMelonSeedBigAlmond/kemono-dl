package moe.peanutmelonseedbigalmond.kemondl.component

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

object DateAdapter : JsonDeserializer<Date?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type, context: JsonDeserializationContext): Date? {
        if (json==null) return null

        if (typeOfT!=Date::class.java) return context.deserialize(json, typeOfT)

        if (!json.isJsonPrimitive) return null

        val primitive = json.asJsonPrimitive
        if (primitive.isString) return sdf.parse(primitive.asString)
        if (!primitive.isNumber) return null

        val number=primitive.asNumber
        if (isFixedPointNumber(number)) {
            return Date(number.toLong())
        }else{
            return Date((number.toDouble()*1000).toLong())
        }
    }

    // judge a number is fixed-point number
    private fun isFixedPointNumber(number: Number): Boolean = number.toDouble()==number.toLong().toDouble()

    @JvmStatic
    private val sdf=SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
}