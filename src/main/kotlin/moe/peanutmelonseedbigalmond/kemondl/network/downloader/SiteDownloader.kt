package moe.peanutmelonseedbigalmond.kemondl.network.downloader

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class SiteDownloader(
    private val uid: String,
    override val service: String,
    coroutineContext: CoroutineContext
) : BaseSiteDownloader(coroutineContext) {
    override suspend fun download() {
        getUserPosts(uid, service)
            .collect {
                download(it)
            }

    }

    companion object {
        @JvmStatic
        suspend fun create(uid: String, service: String): SiteDownloader {
            return SiteDownloader(uid, service, coroutineContext)
        }
    }
}