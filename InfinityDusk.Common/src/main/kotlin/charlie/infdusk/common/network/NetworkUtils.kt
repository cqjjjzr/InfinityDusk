package charlie.infdusk.common.network

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandler
import io.netty.channel.ChannelInitializer
import io.netty.util.ReferenceCountUtil

fun inboundHandler(block: InboundHandlerKotlinAdapter.() -> Unit): ChannelInboundHandler =
        InboundHandlerKotlinAdapter().apply(block)

@Suppress("DEPRECATION")
class InboundHandlerKotlinAdapter : ChannelInboundHandler {
    private var channelActiveBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var channelInactiveBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var userEventTriggeredBlock: ((ChannelHandlerContext, Any?) -> Unit?)? = null
    private var channelWritabilityChangedBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var channelReadBlock: ((ChannelHandlerContext, Any?) -> Unit)? = null
    private var channelRegisteredBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var channelUnregisteredBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var channelReadCompleteBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var handlerAddedBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var handlerRemovedBlock: ((ChannelHandlerContext) -> Unit)? = null
    private var exceptionCaughtBlock: ((ChannelHandlerContext, Throwable) -> Unit)? = null

    fun channelActive(block: (ChannelHandlerContext) -> Unit) {
        channelActiveBlock = block
    }
    override fun channelActive(ctx: ChannelHandlerContext) {
        channelActiveBlock?.invoke(ctx)
    }

    fun channelInactive(block: (ChannelHandlerContext) -> Unit) {
        channelInactiveBlock = block
    }
    override fun channelInactive(ctx: ChannelHandlerContext) {
        channelInactiveBlock?.invoke(ctx)
    }

    fun userEventTriggered(block: (ChannelHandlerContext, Any?) -> Unit) {
        userEventTriggeredBlock = block
    }
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
        userEventTriggeredBlock?.invoke(ctx, evt)
    }

    fun channelWritabilityChanged(block: (ChannelHandlerContext) -> Unit) {
        channelWritabilityChangedBlock = block
    }
    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        channelWritabilityChangedBlock?.invoke(ctx)
    }

    fun channelRead(block: (ChannelHandlerContext, Any?) -> Unit) {
        channelReadBlock = block
    }
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        channelReadBlock?.invoke(ctx, msg)
    }

    fun channelRegistered(block: (ChannelHandlerContext) -> Unit) {
        channelRegisteredBlock = block
    }
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        channelRegisteredBlock?.invoke(ctx)
    }

    fun channelUnregistered(block: (ChannelHandlerContext) -> Unit) {
        channelUnregisteredBlock = block
    }
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        channelUnregisteredBlock?.invoke(ctx)
    }

    fun channelReadComplete(block: (ChannelHandlerContext) -> Unit) {
        channelReadCompleteBlock = block
    }
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        channelReadCompleteBlock?.invoke(ctx)
    }

    fun handlerAdded(block: (ChannelHandlerContext) -> Unit) {
        handlerAddedBlock = block
    }
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        handlerAddedBlock?.invoke(ctx)
    }

    fun handlerRemoved(block: (ChannelHandlerContext) -> Unit) {
        handlerRemovedBlock = block
    }
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        handlerRemovedBlock?.invoke(ctx)
    }

    fun exceptionCaught(block: (ChannelHandlerContext, Throwable) -> Unit) {
        exceptionCaughtBlock = block
    }
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        exceptionCaughtBlock?.invoke(ctx, cause)
    }
}

fun <T: Channel> channelInitializer(block: T.(ChannelInitializer<T>) -> Unit) = object : ChannelInitializer<T>() {
    override fun initChannel(ch: T) {
        ch.block(this)
    }
}

fun releaseMessage(obj: Any) = ReferenceCountUtil.release(obj)