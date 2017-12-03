package charlie.infdusk.core.model

import java.time.Duration
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "contest")
data class Contest(
        // === Hibernate Visible ===
        @Id
        @SequenceGenerator(name = "contest_gen", sequenceName = "contest_gen")
        @GeneratedValue(generator = "contest_gen")
        var id: Int = 0,

        var title: String,
        var descriptionHtml: String,

        var startTime: Instant,
        var endTime: Instant,

        @Enumerated(EnumType.STRING)
        var type: ContestType,
        @ManyToMany
        @OrderColumn(name = "ordinary")
        @JoinTable(name = "contest_problems")
        var problems: List<Problem> = ArrayList()
) {

    // === Kotlin ONLY ===
    val duration: Duration
        @Transient get() = Duration.between(endTime, startTime)

    // === Methods ===
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Contest
        return id == other.id
    }

    override fun hashCode(): Int = id
}

enum class ContestType {
    NOI, IOI
}

class ContestTimeComparator: Comparator<Contest> {
    override fun compare(o1: Contest?, o2: Contest?): Int {
        if (o1 == o2) return 0
        if (o1 == null) return -1
        if (o2 == null) return 1
        return if (Duration.between(o1.startTime, o2.endTime).isNegative) -1 else 1
    }
}
