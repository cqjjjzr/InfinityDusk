package charlie.infdusk.server.auth

import charlie.infdusk.common.auth.LoginRequest
import charlie.infdusk.core.model.User
import charlie.infdusk.server.database.getUserFromName

data class AuthResult(
        val success: Boolean,
        val user: User?
)

private val failAuthResult = AuthResult(false, null)
fun checkAuth(request: LoginRequest): AuthResult {
    val user = getUserFromName(request.username)
            ?: return failAuthResult
    if (user.password != generatePassword(request.password, user.salt))
        return failAuthResult
    return AuthResult(true, user)
}