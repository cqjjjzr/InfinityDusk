package charlie.infdusk.server.session

import charlie.infdusk.server.serverSessions
import io.netty.channel.Channel
import io.netty.util.AttributeKey
import java.net.InetSocketAddress
import java.util.*

val sessionChannelKey = AttributeKey.newInstance<Session>("infdSession")!!

data class Session(
        var networkChannel: Channel
) {
    val sessionID: UUID = UUID.randomUUID()
    var uid: Int = -1

    var status: SessionStatus = SessionStatus.CONNECTED

    val ipAddress get() = (networkChannel.remoteAddress() as InetSocketAddress?)?.address
}

enum class SessionStatus {
    CONNECTED, LOGGED, DOWN
}

fun prepareSession(uid: Int, channel: Channel): Session {
    return Session(channel).apply {
        this.uid = uid
        status = SessionStatus.LOGGED

        serverSessions[sessionID] = this
    }
}