package charlie.infdusk.server.auth

import charlie.infdusk.common.auth.LoginRequest
import charlie.infdusk.core.model.User
import charlie.infdusk.server.database.getUserFromName

fun checkAuthOrNull(request: LoginRequest): User? {
    val userOptional = getUserFromName(request.username)
    if (!userOptional.isPresent) return null
    val user = userOptional.get()
    if (user.password != generatePassword(request.password, user.salt))
        return null
    return user
}