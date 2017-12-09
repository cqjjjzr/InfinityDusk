@file:JvmName("ServerNetwork")
package charlie.infdusk.server.network

import charlie.infdusk.common.network.*
import charlie.infdusk.server.INFDUSK_SERVER_VERSION
import charlie.infdusk.server.serverSessions
import charlie.infdusk.server.session.sessionChannelKey
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler

private val bootstrap: ServerBootstrap = ServerBootstrap()
private var future: ChannelFuture? = null
private val dispatcher =  Dispatcher()

fun initServerNetwork() {
    bootstrap.group(NioEventLoopGroup(), NioEventLoopGroup())
            .channel(NioServerSocketChannel::class.java)
            .childHandler(channelInitializer<SocketChannel> {
                pipeline()
                        .addLast(IdleStateHandler(0, 0, 30))
                        .addLast(inboundHandler {
                            channelActive {
                                it.writeAndFlush(NetworkMessage(
                                        NetworkMessageTypes.SERVER_VERSION,
                                        INFDUSK_SERVER_VERSION))
                            }

                            channelRead { ctx, msg ->
                                if (msg is NetworkMessage)
                                    dispatcher.dispatch(msg, ctx)
                            }

                            channelInactive { ctx ->
                                ctx.channel().attr(sessionChannelKey).get()?.apply {
                                    serverSessions -= sessionID
                                }
                            }

                            userEventTriggered { ctx, evt ->
                                if (evt is IdleStateEvent)
                                    ctx.close()
                            }
                        })
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
}

fun startServerNetwork(port: Int = 9980) {
    if (future != null)
        throw IllegalStateException("Server already started!")
    future = bootstrap.bind(port)
}

fun addNetworkMessageListener(listener: MessageListener) { dispatcher += listener }
fun removeNetworkMessageListener(listener: MessageListener) { dispatcher -= listener }