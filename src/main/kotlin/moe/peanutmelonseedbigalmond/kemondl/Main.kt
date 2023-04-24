package moe.peanutmelonseedbigalmond.kemondl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.peanutmelonseedbigalmond.kemondl.component.database.Database
import moe.peanutmelonseedbigalmond.kemondl.network.Aria2ClientController
import moe.peanutmelonseedbigalmond.kemondl.network.downloader.FavouritesDownloader
import moe.peanutmelonseedbigalmond.kemondl.network.downloader.SiteDownloader

fun main() {
    runBlocking {
        val downloadSettings = App.config.downloadSettings
        if (downloadSettings != null) {
            val users = downloadSettings.users
            val singleUsers = users ?: listOf() // 单独下载的

            val favouriteSettings = downloadSettings.favourite
            if (favouriteSettings != null && favouriteSettings.enabled) {
                launch {
                    FavouritesDownloader.create()
                        .except(singleUsers)
                        .download()
                }
            }

            singleUsers.forEach {
                launch {
                    SiteDownloader.create(it.id, it.site)
                        .download()
                }
            }
        }
    }
    Aria2ClientController.close()
    Database.close()
}