package charlie.infdusk.server

import charlie.infdusk.server.session.Session
import com.google.gson.Gson
import java.util.*

const val INFDUSK_SERVER_VERSION = "0.1-unstable"
val gson = Gson()

val serverSessions: MutableMap<UUID, Session> = HashMap()
