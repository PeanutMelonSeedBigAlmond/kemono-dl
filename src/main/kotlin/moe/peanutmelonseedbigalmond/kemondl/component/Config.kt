package moe.peanutmelonseedbigalmond.kemondl.component

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute

@XStreamAlias("config")
class Config {
    lateinit var session: String

    @XStreamAlias("aria2")
    lateinit var aria2Config: Aria2Config

    @XStreamAlias("download")
    var downloadSettings: DownloadSettings? = null

    @XStreamAlias("aria2")
    class Aria2Config {
        lateinit var host: String
        lateinit var port: String
        var downloadPath: String? = null
        var token: String? = null
    }

    @XStreamAlias("download")
    class DownloadSettings {
        var favourite: DownloadFavouriteSettings? = null

        var users: List<UserSettings>? = null

        @XStreamAlias("favourite")
        class DownloadFavouriteSettings {
            var enabled = false
        }

        @XStreamAlias("user")
        class UserSettings {
            @XStreamAsAttribute
            lateinit var site: String

            @XStreamAsAttribute
            lateinit var id: String
        }
    }
}