@file:JvmName("DatabaseProvider")
package charlie.infdusk.server.database

import charlie.infdusk.core.model.*
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.hibernate.service.ServiceRegistry
import java.util.*

private const val FETCH_SIZE = "50"

private var databaseRegistry: ServiceRegistry?= null
private var databaseSessionFactory: SessionFactory? = null

fun initDatabase(
        hostWithPort: String,
        databaseName: String,
        username: String,
        password: String,
        timezone: TimeZone = TimeZone.getDefault(),
        poolSize: Int = 20
) {
    if (databaseSessionFactory != null) throw IllegalStateException("Database is already initalized!")
    databaseRegistry = StandardServiceRegistryBuilder()
            .applySettings(Configuration()
                    .setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver")
                    .setProperty(Environment.URL, "jdbc:mysql://$hostWithPort/$databaseName?useLegacyDatetimeCode=false&serverTimezone=${timezone.id}")
                    .setProperty(Environment.USER, username)
                    .setProperty(Environment.PASS, password)
                    .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect")
                    .setProperty(Environment.POOL_SIZE, poolSize.toString())
                    .setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                    .setProperty(Environment.STATEMENT_FETCH_SIZE, FETCH_SIZE)
                    .addClass(Contest::class.java)
                    .addClass(JudgeNode::class.java)
                    .addClass(Problem::class.java)
                    .addClass(RunResult::class.java)
                    .addClass(RunStatus::class.java)
                    .addClass(TestCase::class.java)
                    .addClass(User::class.java)
                    .properties)
            .build()
    databaseSessionFactory = MetadataSources(databaseRegistry).metadataBuilder.build().buildSessionFactory()
}
fun shutdownDatabase() {
    if (databaseSessionFactory == null) throw IllegalStateException("Database not initalized!")
    StandardServiceRegistryBuilder.destroy(databaseRegistry!!)
    databaseRegistry = null
    databaseSessionFactory = null
}

fun getUserFromName(username: String): Optional<User> {
    return databaseSessionFactory!!.openSession()
            .createQuery("from User where username=:username", User::class.java)
            .apply {
                setParameter("username", username)
            }.uniqueResultOptional()
}