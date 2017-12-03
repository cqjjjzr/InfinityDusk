package charlie.infdusk.core.model

import javax.persistence.*

@Entity
data class TestCase(
        // === Hibernate Visible ===
        @Id
        @SequenceGenerator(name = "testcase_gen", sequenceName = "testcase_gen")
        @GeneratedValue(generator = "testcase_gen")
        var tid: Int = 0,

        @ManyToOne
        var enclosingProblem: Problem,

        @Lob
        var input: String,
        @Lob
        var output: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TestCase
        return tid == other.tid
    }

    override fun hashCode(): Int = tid
}