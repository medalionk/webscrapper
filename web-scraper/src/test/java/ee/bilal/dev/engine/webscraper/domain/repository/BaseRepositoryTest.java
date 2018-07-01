package ee.bilal.dev.engine.webscraper.domain.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test.properties")
@AutoConfigureTestDatabase(replace = NONE)
public abstract class BaseRepositoryTest<T> {
    @Autowired
    TestEntityManager entityManager;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    <E> void persistList(List<E> list){
        for (E e : list) {
            entityManager.persist(e);
        }

        entityManager.flush();
    }
}
