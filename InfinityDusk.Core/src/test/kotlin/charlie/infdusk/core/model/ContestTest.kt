package charlie.infdusk.core.model

import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.junit.Test
import java.time.Instant
import java.util.*

class ContestTest {
    @Test
    fun testBasic() {
        MetadataSources(
                StandardServiceRegistryBuilder()
                        .configure(javaClass.getResource("/hibernate,cfg,xml"))
                        .build())
                .buildMetadata()
                .buildSessionFactory()
                .apply {
                    val session = openSession()
                    val temp = ArrayList<Problem>()
                    val t = session.beginTransaction()
                    val c1 = Contest(0,
                            "Test",
                            "test!!!",
                            Instant.now(),
                            Instant.now().plusSeconds(60 * 60),
                            ContestType.IOI,
                            temp).apply {
                        temp += Problem(0,
                                Arrays.asList(this),
                                "Title",
                                "Test",
                                specialJudgeJavascript = "PN").apply {
                            samples = Arrays.asList(TestCase(0, this,  "1 2\n", "3\n")
                                    .apply { session.save(this) })
                            testCases = Arrays.asList(TestCase(0, this, "2 3\n", "5\n")
                                    .apply { session.save(this) })
                            session.save(this)
                        }
                        temp += Problem(0,
                                Arrays.asList(this),
                                "Title2",
                                "Test2",
                                specialJudgeJavascript = "PN").apply {
                            samples = Arrays.asList(TestCase(0, this,  "1 2\n", "3\n")
                                    .apply { session.save(this) })
                            testCases = Arrays.asList(TestCase(0, this, "2 3\n", "5\n")
                                    .apply { session.save(this) })
                            session.save(this)
                        }
                    }
                    val c2 = Contest(0,
                            "Test222",
                            "test!!!",
                            Instant.now().minusMillis(5000),
                            Instant.now().plusSeconds(60 * 60),
                            ContestType.NOI,
                            emptyList())
                    session.save(c1)
                    session.save(c2)
                    session.save(User(0,
                            "Charlie",
                            "iosjgjgjzmdfg",
                            "sghiomsdfgzdfg",
                            "CharlieJiang",
                            null,
                            "!",
                            sortedSetOf(ContestTimeComparator(), c1, c2)))
                    t.commit()
                }
    }
}
