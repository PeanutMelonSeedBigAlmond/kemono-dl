package moe.peanutmelonseedbigalmond.kemondl

import moe.peanutmelonseedbigalmond.kemondl.component.Config
import moe.peanutmelonseedbigalmond.kemondl.component.xml.XmlUtil
import java.io.FileInputStream

object App {
    @JvmStatic
    lateinit var config: Config
        private set

    init {
        loadConfig()
    }

    @JvmStatic
    private fun loadConfig(){
        FileInputStream("config.xml").use {
            config=XmlUtil.fromXml(it,Config::class.java)
        }
    }
}