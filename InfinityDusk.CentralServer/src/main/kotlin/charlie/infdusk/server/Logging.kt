package charlie.infdusk.server

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.text.MessageFormat
import java.time.Instant
import java.util.logging.FileHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.util.logging.Logger

val LOGGER_MAX_BYTES = 1 * 1024 * 1024 // 1 MBytes

fun initLogger() {
    listOf(
            "infd.Network",
            "infd.Judge",
            "infd.Core",
            "infd.Database",
            "infd.Processing"
    ).map { Logger.getLogger(it) }
            .forEach {
                it.useParentHandlers = false
                it.addHandler(FileHandler("infDusk-latest.log", LOGGER_MAX_BYTES, 1, false)
                        .apply { formatter = InfDuskLogFormatter() })
            }
}

private class InfDuskLogFormatter : Formatter() {
    override fun format(record: LogRecord?): String {
        if (record == null) return ""
        var str = MessageFormat.format("[{0}] [{1}] [{2}] {3}",
                record.level.name,
                Instant.ofEpochMilli(record.millis),
                record.loggerName,
                record.message)
        if (record.thrown != null) {
            str += "\n" +
                    ByteArrayOutputStream().apply {
                        str += record.thrown.printStackTrace(PrintStream(this))
                    }.toString("UTF-8")
        }
        return str
    }
}
