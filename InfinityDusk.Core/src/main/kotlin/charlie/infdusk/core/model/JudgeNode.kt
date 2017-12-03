package charlie.infdusk.core.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
data class JudgeNode(
        @Id
        @SequenceGenerator(name = "problems_gen", sequenceName = "problems_gen")
        @GeneratedValue(generator = "problems_gen")
        var id: Int = 0,
        var ipAddress: String,
        var port: String,

        var passwordSHA1: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as JudgeNode
        if (id == other.id) return true
        return ipAddress == other.ipAddress && port == other.port
    }

    override fun hashCode(): Int = id
}