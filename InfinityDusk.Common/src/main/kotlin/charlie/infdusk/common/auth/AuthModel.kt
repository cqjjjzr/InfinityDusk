package charlie.infdusk.common.auth

data class LoginRequest(
        val username: String,
        val password: String
)

const val LOGIN_RESPONSE_SUCCESS = "success"
const val LOGIN_RESPONSE_FAILED = "failed"