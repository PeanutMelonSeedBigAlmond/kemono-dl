package moe.peanutmelonseedbigalmond.aria2

import com.google.gson.annotations.SerializedName
import java.util.*

class Aria2Request internal constructor(
    @SerializedName("method")
    val methodName: String,
    @SerializedName("params")
    val parameters: Array<Any>,
    @SerializedName("id")
    val requestId: String = UUID.randomUUID().toString().replace("-", "")
) {
    @SerializedName("jsonrpc")
    @Suppress("UNUSED")
    private val rpcVersion = "2.0"

    class Builder {
        private var methodName: String? = null
        private var token: String? = null
        private var arguments: Array<Any>? = null

        fun methodName(methodName: String): Builder {
            this.methodName = methodName
            return this
        }

        fun token(token: String): Builder {
            this.token = token
            return this
        }

        fun arguments(arguments: Array<Any>): Builder {
            this.arguments = arguments
            return this
        }

        fun build(): Aria2Request {
            requireNotNull(arguments) { "arguments == null" }
            val args = if (token.isNullOrEmpty())
                this.arguments!!
            else arrayOf<Any>(this.token!!, this.arguments!!)

            return Aria2Request(
                requireNotNull(methodName) { " methodName == null " },
                args
            )
        }
    }
}