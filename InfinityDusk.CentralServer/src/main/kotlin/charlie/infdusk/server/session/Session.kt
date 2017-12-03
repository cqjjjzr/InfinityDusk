package charlie.infdusk.server.session

import io.netty.channel.Channel
import java.net.InetSocketAddress
import java.net.SocketAddress

data class Session(
        var networkChannel: Channel
) {
    lateinit var commKey: ByteArray
    var uid: Int,

    var status: SessionStatus = SessionStatus.CONNECTED

    val ipAddress get() = (networkChannel.remoteAddress() as InetSocketAddress?)?.address
}

enum class SessionStatus {
    CONNECTED, LOGGED, DOWN
}

fun Session.handleLogin() {
    
}