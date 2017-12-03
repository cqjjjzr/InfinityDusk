package charlie.infdusk.server.network

import charlie.infdusk.common.network.NetworkMessage
import charlie.infdusk.common.network.NetworkMessageTypes
import charlie.infdusk.common.network.channelInitializer
import charlie.infdusk.common.network.inboundHandler
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
}