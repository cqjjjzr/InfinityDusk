package charlie.infdusk.core.model

import java.util.*
import javax.persistence.*

@Entity
data class Problem(
        @Id
        @SequenceGenerator(name = "problems_gen", sequenceName = "problems_gen")
        @GeneratedValue(generator = "problems_gen")
        var id: Int = 0,
        @ManyToMany(mappedBy = "problems")
        var enclosingContests: List<Contest>,

        var title: String = "",
        var descriptionHtml: String = "",
        var timeLimitMs: Int = 1000,
        var memoryLimitByte: Int = 65536,

        var supportedLanguagesAsString: String
            = ProgrammingLanguage.values().joinToString("|") { it.id.toString() },

        var specialJudgeJavascript: String? = null,

        @OneToMany(mappedBy = "enclosingProblem")
        @OrderColumn(name = "ordinary")
        var samples: List<TestCase> = ArrayList(),
        @OneToMany(mappedBy = "enclosingProblem")
        @OrderColumn(name = "ordinary")
        var testCases: List<TestCase> = ArrayList()
) {
    // === Kotlin only ===
    var supportedLanguages: Set<ProgrammingLanguage>
        @Transient
        get() = supportedLanguagesAsString.split("|").map { ProgrammingLanguage.valueOf(it) }.toSet()
        set(value) { supportedLanguagesAsString = value.joinToString("|") }

    // === Methods ===
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Problem
        return id == other.id
    }

    override fun hashCode(): Int = id
}