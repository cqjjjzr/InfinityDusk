package charlie.infdusk.common.network

import io.netty.channel.ChannelHandlerContext
import java.util.*

class Dispatcher {
    private val listeners = LinkedList<MessageListener>()

    operator fun plusAssign(listener: MessageListener) {
        listeners += listener
    }

    operator fun minusAssign(listener: MessageListener) {
        listeners -= listener
    }

    fun dispatch(message: NetworkMessage, context: ChannelHandlerContext) {
        listeners
                .filter { it.acceptType(message.messageType) }
                .forEach { if (!it.messageIncoming(message, context)) return@dispatch }
    }
}

interface MessageListener {
    fun acceptType(type: NetworkMessageTypes): Boolean
    fun messageIncoming(message: NetworkMessage, context: ChannelHandlerContext): Boolean
}

fun messageListener(vararg types: NetworkMessageTypes,
                    block: (NetworkMessage, ChannelHandlerContext) -> Boolean) = object : MessageListener {
    override fun messageIncoming(message: NetworkMessage, context: ChannelHandlerContext): Boolean {
        return block(message, context)
    }
    override fun acceptType(type: NetworkMessageTypes): Boolean = types.contains(type)
}