package charlie.infdusk.common.network

enum class NetworkMessageTypes(val typeID: Int) {
    SERVER_VERSION(0x00),

    LOGIN_REQUEST(0x01), LOGIN_RESPONSE(0x81);

    companion object {
        fun forTypeID(typeID: Int) = NetworkMessageTypes.values().firstOrNull { it.typeID == typeID }
                ?: throw NoSuchElementException("typeID: " + typeID)
        operator fun get(typeID: Int) = forTypeID(typeID)
    }
}