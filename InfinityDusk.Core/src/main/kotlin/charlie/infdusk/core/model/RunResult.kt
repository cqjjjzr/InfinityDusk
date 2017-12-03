package charlie.infdusk.core.model

import javax.persistence.*

@Entity
data class RunResult(
        @Id
        @SequenceGenerator(name = "result_gen", sequenceName = "result_gen")
        @GeneratedValue(generator = "result_gen")
        var id: Int = 0,

        var contestID: Int,
        var problemID: Int,

        @Lob
        var code: String,
        var programmingLanguageAsInt: Int,

        @Enumerated(EnumType.STRING)
        var status: RunStatus,
        @Lob
        var returnMessage: String,
        var timeUsedMs: Int?,
        var memoryMaxUsed: Int?,

        var allocatedJudgerID: Int = -1,
        var retryCount: Int = 0
) {
    // === Kotlin only ===
    var programmingLanguage: ProgrammingLanguage
        @Transient
        get() = ProgrammingLanguage.fromId(programmingLanguageAsInt)
        set(value) { programmingLanguageAsInt = value.id }

    // === Methods ===
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RunResult
        return id == other.id
    }

    override fun hashCode(): Int = id
}

enum class RunStatus {
    WAITING, COMPILING, RUNNING,
    ACCEPTED, WRONG_ANSWER, PRESENTATION_ERROR,
    COMPILE_ERROR, RUNTIME_ERROR,
    TIME_LIMIT_EXCEED, MEMORY_LIMIT_EXCEED, OUTPUT_LIMIT_EXCEED,
    INTERNAL_ERROR
}
