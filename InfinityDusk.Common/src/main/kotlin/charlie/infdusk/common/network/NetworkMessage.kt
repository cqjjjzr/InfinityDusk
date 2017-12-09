package charlie.infdusk.common.network

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToByteEncoder
import java.nio.charset.Charset
import java.util.zip.CRC32

val UTF_8 = Charset.forName("UTF-8")

data class NetworkMessage(
                          val messageType: NetworkMessageTypes,
                          val body: String)

private const val MESSAGE_HEADER_LENGTH = 4 + 4 + 4
class NetworkMessageDecoder: ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (buf.readableBytes() < MESSAGE_HEADER_LENGTH) return
        val messageType = buf.readInt()

        val bodyLength = buf.readInt()
        val bodyCRC32 = buf.readInt()

        if (buf.readableBytes() < bodyLength) return
        val body = buf.readCharSequence(bodyLength, UTF_8).toString()
        CRC32().apply {
            update(body.toByteArray())
            if (bodyCRC32 != value.toInt()) return
        }

        out += NetworkMessage(NetworkMessageTypes[messageType], body)
    }
}

class NetworkMessageEncoder: MessageToByteEncoder<NetworkMessage>(NetworkMessage::class.java) {
    override fun encode(ctx: ChannelHandlerContext, msg: NetworkMessage, out: ByteBuf) {
        out.writeInt(msg.messageType.typeID)

        val bodyBytes = msg.body.toByteArray()
        out.writeInt(bodyBytes.size)
        msg.apply {
            CRC32().apply {
                update(bodyBytes)
                out.writeInt(value.toInt())
            }
        }

        out.writeBytes(bodyBytes)
    }
}