package charlie.infdusk.server.database

import org.hibernate.Session

fun Session.use(block: (Session) -> Unit) {
    beginTransaction().let {
        try {
            block(this)
            it.commit()
        } catch (ex: Exception) {
            it.rollback()
            throw ex
        }
    }
}