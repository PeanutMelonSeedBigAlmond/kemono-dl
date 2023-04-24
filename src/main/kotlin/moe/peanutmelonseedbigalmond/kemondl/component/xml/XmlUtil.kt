package moe.peanutmelonseedbigalmond.kemondl.component.xml

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import java.io.InputStream

object XmlUtil {
    @JvmStatic
    fun toXml(obj: Any): String {
        val xStream= XStream()
        xStream.autodetectAnnotations(true)

        return xStream.toXML(obj)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> fromXml(inputStream:InputStream,targetClass: Class<T>):T{
        val xStream=XStream()
        xStream.addPermission(AnyTypePermission.ANY)
        xStream.processAnnotations(targetClass)
        return xStream.fromXML(inputStream) as T
    }
}