package charlie.infdusk.core.model

enum class ProgrammingLanguage(val id: Int) {
    C(1), CPP(2), CPP11(3), Java(4);
    companion object {
        fun fromId(id: Int) = ProgrammingLanguage.values().firstOrNull { it.id == id }
                ?: throw NoSuchElementException(id.toString())
    }
}
