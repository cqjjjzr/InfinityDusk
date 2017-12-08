@file:JvmName("AuthNetwork")
package charlie.infdusk.server.auth

import charlie.infdusk.common.auth.LoginRequest
import charlie.infdusk.common.auth.LoginResponse
import charlie.infdusk.common.network.NetworkMessage
import charlie.infdusk.common.network.NetworkMessageTypes
import charlie.infdusk.common.network.messageListener
import charlie.infdusk.server.gson
import charlie.infdusk.server.network.addNetworkMessageListener
import charlie.infdusk.server.network.emptyUUID
import charlie.infdusk.server.session.prepareSession
import charlie.infdusk.server.session.sessionChannelKey


fun initAuthNetwork() {
    addNetworkMessageListener(messageListener(NetworkMessageTypes.LOGIN_REQUEST) { msg, ctx ->
        val req = gson.fromJson<LoginRequest>(msg.body, LoginRequest::class.java)
        val (success, user) = checkAuth(req);
        if (!success) {
            ctx.writeAndFlush(NetworkMessage(
                    emptyUUID,
                    NetworkMessageTypes.LOGIN_RESPONSE,
                    gson.toJson(LoginResponse(false, ""))))
            ctx.close()
        } else {
            ctx.channel().attr(sessionChannelKey).set(prepareSession(user!!.uid, ctx.channel()))
        }
        false
    })
}