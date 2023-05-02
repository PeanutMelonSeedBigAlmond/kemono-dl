package moe.peanutmelonseedbigalmond.kemondl.network

import moe.peanutmelonseedbigalmond.aria2.Aria2Callback
import moe.peanutmelonseedbigalmond.aria2.Aria2Client
import moe.peanutmelonseedbigalmond.aria2.Aria2Error
import moe.peanutmelonseedbigalmond.aria2.Aria2TaskInfo
import moe.peanutmelonseedbigalmond.kemondl.App
import moe.peanutmelonseedbigalmond.kemondl.ConsoleLoggerFormatter
import java.io.Closeable
import java.util.logging.Logger
import kotlin.math.roundToInt

object Aria2ClientController : Aria2Callback, Closeable {
    @JvmStatic
    private val logger = ConsoleLoggerFormatter.installFormatter(Logger.getLogger(this::class.simpleName))

    @JvmStatic
    var useSystemProxy = false

    @JvmStatic
    private val aria2Client by lazy {
        val host = App.config.aria2Config.host
        val port = App.config.aria2Config.port.toInt()
        val token = App.config.aria2Config.token ?: ""
        return@lazy Aria2Client(host, port, token = token, callback = this, logger = this.logger)
    }

    override fun onDownloadStart(taskInfo: Aria2TaskInfo) {
        logger.info("${taskInfo.path} 开始下载")
    }

    override fun onDownloadComplete(taskInfo: Aria2TaskInfo) {
        val (size, sizeUnit) = when {
            taskInfo.completedLength > 1024 * 1024 * 1024 -> Pair(taskInfo.completedLength / 1024.0 / 1024 / 1024, "GB")
            taskInfo.completedLength > 1024 * 1024 -> Pair(taskInfo.completedLength / 1024.0 / 1024, "MB")
            taskInfo.completedLength > 1024 -> Pair(taskInfo.completedLength / 1024.0, "KB")
            else -> Pair(taskInfo.completedLength.toDouble(), "B")
        }
        logger.info("${taskInfo.path} 下载完成 (${(size * 100).roundToInt() / 100.0} $sizeUnit)")
    }

    override fun onDownloadError(taskInfo: Aria2TaskInfo) {
        logger.warning("${taskInfo.path} 下载失败: ${Aria2Error.getError(taskInfo.errorCode)} (${taskInfo.errorCode})")
    }

    override fun onDownloadStop(taskInfo: Aria2TaskInfo) {
        logger.info("${taskInfo.path} 暂停下载")
    }

    override fun close() {
        aria2Client.close()
    }

    fun addUri(
        uri: String,
        dir: String = if (App.config.aria2Config.downloadPath.isNullOrBlank()) System.getProperty("user.dir") else App.config.aria2Config.downloadPath!!,
        filename: String = "",
        options: Map<String, Any> = mapOf(),
    ): String {
        val opt = options.toMutableMap()
        if (useSystemProxy) {
            getHttpProxy()?.let { opt["http-proxy"] = it }
            getHttpsProxy()?.let { opt["https-proxy"] = it }
        }
        return aria2Client.addUri(uri, dir, filename, opt)
    }

    private fun getHttpProxy(): String? {
        val proxy =
            System.getProperty("http.proxyHost")?.plus(":")?.plus(System.getProperty("http.proxyPort", "80"))?.let {
                "http://$it"
            }
        return proxy ?: System.getenv("http_proxy") ?: System.getProperty("HTTP_PROXY") ?: null
    }

    private fun getHttpsProxy(): String? {
        val proxy =
            System.getProperty("https.proxyHost")?.plus(":")?.plus(System.getProperty("https.proxyPort", "443"))?.let {
                "http://$it"
            }
        return proxy ?: System.getenv("https_proxy") ?: System.getProperty("HTTPS_PROXY") ?: null
    }

    fun removeTask(id: String): String = aria2Client.removeTask(id)

    fun pause(id: String): String = aria2Client.pause(id)

    fun getTaskInfo(gid: String): Aria2TaskInfo = aria2Client.getTaskInfo(gid)
}