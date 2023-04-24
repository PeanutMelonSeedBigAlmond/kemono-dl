package moe.peanutmelonseedbigalmond.kemondl.network.downloader

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.transform
import moe.peanutmelonseedbigalmond.kemondl.component.Config
import moe.peanutmelonseedbigalmond.kemondl.network.Client
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class FavouritesDownloader private constructor(
    coroutineContext: CoroutineContext
) : BaseDownloader(coroutineContext) {

    private val exceptUsers = HashSet<Config.DownloadSettings.UserSettings>()

    override suspend fun download() {
        getFavUsers()
            .filterNot {
                val user = Config.DownloadSettings.UserSettings()
                user.site = it.site
                user.id = it.creatorId
                return@filterNot exceptUsers.contains(user)
            }
            .transform {
                emitAll(
                    getUserPosts(it.creatorId, it.site)
                )
            }
            .collect {
                download(it)
            }
    }

    fun except(user: Config.DownloadSettings.UserSettings): FavouritesDownloader {
        this.exceptUsers.add(user)
        return this
    }

    fun except(users: List<Config.DownloadSettings.UserSettings>): FavouritesDownloader {
        this.exceptUsers.addAll(users)
        return this
    }

    private suspend fun getFavUsers() = Client.getFavourites().asFlow()

    companion object {
        @JvmStatic
        suspend fun create(): FavouritesDownloader {
            return FavouritesDownloader(coroutineContext)
        }
    }
}