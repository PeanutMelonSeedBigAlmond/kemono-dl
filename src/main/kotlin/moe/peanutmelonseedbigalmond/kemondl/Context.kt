package moe.peanutmelonseedbigalmond.kemondl

import java.io.File

object Context {
    @JvmStatic
    val cwd: String
        get() = System.getProperty("user.dir")

    @JvmStatic
    val downloadDir: String
        get() = File(cwd, "kemono-dl").canonicalPath
}