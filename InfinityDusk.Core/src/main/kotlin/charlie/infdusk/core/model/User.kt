package charlie.infdusk.core.model

import java.awt.Image
import javax.persistence.*

@Entity
data class User(
        @Id
        @SequenceGenerator(name = "user_gen", sequenceName = "user_gen")
        @GeneratedValue(generator = "user_gen")
        var uid: Int = 0,

        var username: String,
        var passwordSHA1: String,
        var salt: String,

        var nickname: String,
        @Lob
        @Column(nullable = true)
        var avatar: Image?,
        @Lob
        var signatureHtml: String,

        @OneToMany
        @JoinTable(name="join_contest")
        //@SortComparator(ContestTimeComparator::class)
        @OrderBy("start_time DESC")
        var joinedContests: Set<Contest>
) {
    // === Methods ===
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        return uid == other.uid
    }

    override fun hashCode(): Int = uid
}