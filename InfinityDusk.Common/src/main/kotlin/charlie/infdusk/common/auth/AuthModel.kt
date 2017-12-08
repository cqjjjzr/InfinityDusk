package charlie.infdusk.common.auth

data class LoginRequest(
        val username: String,
        val password: String
)

class LoginResponse(val success: Boolean,
                    val sessionID: String)