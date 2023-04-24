package moe.peanutmelonseedbigalmond.kemondl.network.downloader

import kotlin.coroutines.CoroutineContext

abstract class BaseSiteDownloader(
    coroutineContext: CoroutineContext
) : BaseDownloader(coroutineContext) {
    abstract val service: String
}