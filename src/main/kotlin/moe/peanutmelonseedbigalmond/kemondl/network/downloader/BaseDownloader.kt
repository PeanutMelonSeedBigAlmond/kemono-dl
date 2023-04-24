package moe.peanutmelonseedbigalmond.kemondl.network.downloader

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import moe.peanutmelonseedbigalmond.kemondl.ConsoleLoggerFormatter
import moe.peanutmelonseedbigalmond.kemondl.component.database.Database
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.CompositePostId
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.CreatorBean
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.FileBean
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.PostBean
import moe.peanutmelonseedbigalmond.kemondl.network.Api
import moe.peanutmelonseedbigalmond.kemondl.network.Aria2ClientController
import moe.peanutmelonseedbigalmond.kemondl.network.Client
import moe.peanutmelonseedbigalmond.kemondl.network.response.CreatorsResponse
import moe.peanutmelonseedbigalmond.kemondl.network.response.PostContentResponse
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.nio.charset.Charset
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

abstract class BaseDownloader(
    coroutineContext: CoroutineContext
) : CoroutineScope by CoroutineScope(coroutineContext) {
    private val logger = ConsoleLoggerFormatter.installFormatter(Logger.getLogger(this::class.java.simpleName))

    init {
        Aria2ClientController.useSystemProxy = true
        if (creators == null) {
            runBlocking(coroutineContext) { creators = Client.getAllCreators() }
        }
    }

    abstract suspend fun download()
    protected fun getUserPosts(creatorId: String, service: String) = flow {
        var count = 0
        while (true) {
            val resp = Client.getUserPosts(service, creatorId, offset = count)
            if (resp.isEmpty()) {
                return@flow
            } else {
                resp.forEach {
                    emit(it)
                }
                count += resp.size
            }
        }
    }

    protected fun download(postContentResponse: PostContentResponse) {
        if (checkIfPostInDatabase(postContentResponse)) return

        val username =
            creators!!.first { it.id == postContentResponse.userId && it.service == postContentResponse.site }.name

        val dirName =
            "${postContentResponse.site}/[${filterInvalidChars(username)}/${filterInvalidChars(postContentResponse.title)}"
        val dir = File(dirName)
        if (!dir.exists()) dir.mkdirs()

        FileOutputStream(
            File(
                dir,
                "content.json"
            )
        ).use {
            it.write(gson.toJson(postContentResponse).toByteArray(Charset.defaultCharset()))
        }

        var error = false
        for (attachment in postContentResponse.attachments) {
            val uri = URI("${Api.BASE_IMAGE_URL}/${attachment.path}").normalize().toURL().toString()
            try {
                val gid = Aria2ClientController.addUri(
                    uri = uri,
                    dir = dir.canonicalPath,
                    filename = attachment.name,
                    options = mapOf(
                        "header" to listOf("Referer: ${Api.BASE_IMAGE_URL}"),
                    )
                )
                logger.info("已添加任务: ${attachment.name} ($gid)")
            } catch (e: Exception) {
                logger.warning("添加任务失败: ${attachment.name}")
                logger.warning(e.toString())
                error = true
            }
        }

        postContentResponse.cover?.let {
            val uri = URI("${Api.BASE_IMAGE_URL}/${it.path}").normalize().toURL().toString()
            try {
                Aria2ClientController.addUri(
                    uri = uri,
                    dir = dir.canonicalPath,
                    filename = it.name,
                    options = mapOf(
                        "header" to listOf("Referer: ${Api.BASE_IMAGE_URL}"),
                    )
                )
            } catch (e: Exception) {
                logger.warning("添加任务失败: ${it.name}")
                logger.warning(e.toString())
                error = true
            }
        }

        if (!error) {
            saveToDatabase(postContentResponse, username)
        }
    }

    private fun saveToDatabase(postContentResponse: PostContentResponse, username: String) {
        val post = PostBean().apply {
            title = postContentResponse.title
            content = postContentResponse.content
            key = CompositePostId().apply {
                site = postContentResponse.site
                postId = postContentResponse.postId
                creatorId = postContentResponse.userId
            }
            published = postContentResponse.publishedTime
        }

        val files = postContentResponse.attachments.map {
            return@map FileBean().apply {
                path = it.path
                name = it.name
                type = "attachment"
                this.post = post
            }
        }.toMutableList()
        if (postContentResponse.cover != null) {
            files += FileBean().apply {
                path = postContentResponse.cover.path
                name = postContentResponse.cover.name
                type = "cover"
                this.post = post
            }
        }

        post.files = files

        Database.addPostAndCreator(post, CreatorBean().apply {
            id = postContentResponse.userId
            this.username = username
        })
    }

    private fun checkIfPostInDatabase(postContentResponse: PostContentResponse): Boolean {
        val pk = CompositePostId().apply {
            postId = postContentResponse.postId
            site = postContentResponse.site
            creatorId = postContentResponse.userId
        }

        return Database.findPostByCompositePostId(pk) != null
    }

    private fun filterInvalidChars(fileName: String): String {
        val regex = Regex("[\\\\/:*?\"<>|]+")
        return regex.replace(fileName, "_").trim()
    }

    companion object {
        @JvmStatic
        private val gson = Gson()

        @JvmStatic
        private var creators: List<CreatorsResponse>? = null
    }
}