package charlie.infdusk.server.auth

import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val SALT_LENGTH = 16
private const val PBKDF2_ITERATION_COUNT = 1500
private const val PASSWORD_LENGTH = 128 * 4
fun generateSalt(): String =
        Base64.getEncoder().encodeToString(ByteArray(SALT_LENGTH).apply { SecureRandom().nextBytes(this) })

fun generatePassword(password: String, salt: String) =
        Base64.getEncoder().encodeToString(
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(
                        PBEKeySpec(
                                password.toCharArray(),
                                Base64.getDecoder().decode(salt),
                                PBKDF2_ITERATION_COUNT,
                                PASSWORD_LENGTH)
                ).encoded
        )!!