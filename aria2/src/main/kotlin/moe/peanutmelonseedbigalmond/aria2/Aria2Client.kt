package moe.peanutmelonseedbigalmond.aria2

import com.google.gson.Gson
import moe.peanutmelonseedbigalmond.aria2.response.Aria2Response
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.Closeable
import java.net.URL
import java.util.logging.Logger

class Aria2Client(
    host: String,
    port: Int,
    private val callback: Aria2Callback? = null,
    private val token: String = "",
    private val logger: Logger= Logger.getLogger(Aria2Client::class.simpleName)
) : WebSocketListener(), Closeable {
    private val url = URL("http://$host:$port/jsonrpc")
    private val rpcClient = OkHttpClient()
    private var websocket: WebSocket
    private val connectionLock = Object()
    private val gson = Gson()

    init {
        val wsClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        websocket = wsClient.newWebSocket(request, this)
        synchronized(connectionLock) {
            connectionLock.wait()
        }
    }

    fun getTaskInfo(gid: String): Aria2TaskInfo {
        val response = invokeActual<Aria2Response>(
            Aria2Method.TELL_STATUS,
            arrayOf(gid)
        ).result.asJsonObject

        val fileObj = response.getAsJsonArray("files")[0]
            .asJsonObject

        val completedLength = fileObj["completedLength"].asString.toLong()
        val path = fileObj["path"].asString
        val totalLength = fileObj["length"].asString.toLong()
        val gid = response["gid"].asString
        val errorCode = response["errorCode"].asString.toInt()

        return Aria2TaskInfo(completedLength, path, errorCode, gid, totalLength)
    }

    private inline fun <reified V> invokeActual(
        methodName: String,
        argument: Array<Any>,
        token: String = this.token
    ): V {
        val args = if (token.isEmpty()) argument else arrayOf(token, *argument)
        val data = Aria2Request.Builder()
            .methodName(methodName)
            .arguments(args)
            .token(token)
            .build()
        val request = Request.Builder()
            .url(this.url)
            .post(gson.toJson(data).toRequestBody("application/json-rpc".toMediaType()))
            .build()

        val response = rpcClient.newCall(request)
            .execute().body!!.string()

        return gson.fromJson(response, V::class.java)
    }

    fun addUri(
        uri: String,
        dir: String = System.getProperty("user.dir"),
        filename: String = "",
        options: Map<String, Any> = mapOf(),
    ): String {
        val newOptions = options.toMutableMap()
        newOptions[Aria2Params.OUT_FILE_DIR] = dir
        newOptions[Aria2Params.OUT_FILE_NAME] = filename

        val response = invokeActual<Aria2Response>(
            Aria2Method.ADD_URI,
            arrayOf(arrayOf(uri), newOptions),
        )

        return response.id!!
    }

    fun removeTask(id: String): String {
        val response = invokeActual<Aria2Response>(
            Aria2Method.REMOVE_TASK,
            arrayOf(id),
        )
        return response.id!!
    }

    fun pause(id: String): String {
        val response = invokeActual<Aria2Response>(
            Aria2Method.PAUSE,
            arrayOf(id)
        )
        return response.id!!
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        logger.info("Websocket已连接")
        synchronized(connectionLock) {
            connectionLock.notifyAll()
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        logger.info("Websocket已断开连接")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val data = gson.fromJson(text, Aria2Response::class.java)
        when (data.method) {
            "aria2.onDownloadStop" -> {
                val gid = data.params!![0].asJsonObject["gid"].asString
                val taskInfo = getTaskInfo(gid)
                callback?.onDownloadStop(taskInfo)
            }

            "aria2.onDownloadComplete" -> {
                val gid = data.params!![0].asJsonObject["gid"].asString
                val taskInfo = getTaskInfo(gid)
                callback?.onDownloadComplete(taskInfo)
            }

            "aria2.onDownloadError" -> {
                val gid = data.params!![0].asJsonObject["gid"].asString
                val taskInfo = getTaskInfo(gid)
                callback?.onDownloadError(taskInfo)
            }

            else -> {}
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        logger.warning("Websocket出现异常：$t")
        t.printStackTrace()
    }

    override fun close() {
        websocket.close(1000, null)
    }
}