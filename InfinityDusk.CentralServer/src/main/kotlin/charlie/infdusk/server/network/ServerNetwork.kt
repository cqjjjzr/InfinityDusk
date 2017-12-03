package charlie.infdusk.server.network

import charlie.infdusk.common.network.*
import charlie.infdusk.server.INFDUSK_SERVER_VERSION
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

class ServerNetwork(private val port: Int) {
    private val bootstrap: ServerBootstrap = ServerBootstrap()
    private var future: ChannelFuture? = null
    private val dispatcher =  Dispatcher()

    init {
        bootstrap.group(NioEventLoopGroup(), NioEventLoopGroup())
                .channel(NioServerSocketChannel::class.java)
                .childHandler(channelInitializer<SocketChannel> {
                    pipeline().addLast(inboundHandler {
                        channelActive {
                            it.writeAndFlush(NetworkMessage(
                                    0x00,
                                    NetworkMessageTypes.SERVER_VERSION,
                                    INFDUSK_SERVER_VERSION))
                        }

                        channelRead { ctx, msg ->
                            if (msg is NetworkMessage)
                                dispatcher.dispatch(msg, ctx)
                        }
                    })
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
    }

    fun start() {
        if (future != null)
            throw IllegalStateException("Server already started!")
        future = bootstrap.bind(port)
    }

    fun stop() {
        (future ?: throw IllegalStateException("Server isn't started"))
                .channel().closeFuture().sync()
    }

    fun addListener(listener: MessageListener) { dispatcher += listener }
    fun removeListener(listener: MessageListener) { dispatcher -= listener }
}