package moe.peanutmelonseedbigalmond.kemondl

import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.util.logging.Logger


class ConsoleLoggerFormatter : Formatter() {
    override fun format(record: LogRecord): String {
        val message = formatMessage(record)
        var throwable = ""
        if (record.thrown != null) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            pw.println()
            record.thrown.printStackTrace(pw)
            pw.close()
            throwable = """
                $sw
                """.trimIndent()
        }
        val currentThread = Thread.currentThread()
        val stackTrace = currentThread.stackTrace[8]
        val datetime= getCurrentDateTime()
        return String.format(
            "%s [%s] (%s:%d) %s%s\n",
            datetime,
            Thread.currentThread().name,
            stackTrace.fileName,
            stackTrace.lineNumber,
            message,
            throwable
        )
    }

    companion object {
        @JvmStatic
        fun installFormatter(logger: Logger): Logger {
            logger.useParentHandlers = false
            val consoleHandler = ConsoleHandler()
            consoleHandler.formatter = ConsoleLoggerFormatter()
            logger.addHandler(consoleHandler)
            return logger
        }

        @JvmStatic
        fun getCurrentDateTime(): String {
            val dateTime=LocalDateTime.now()
            return "${dateTime.toLocalDate()} ${dateTime.toLocalTime()}"
        }
    }
}