package ee.bilal.dev.engine.webscraper.domain.repository;

import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class JobResultRepositoryTest extends BaseRepositoryTest{
    private List<JobResult> results = new ArrayList<>();

    @Autowired
    private JobResultRepository jobResultRepository;

    @Before
    public void setUp() {
        results.add(JobResult.of("8a8d4e9f644fbd", "123", "https://cnn.com", "Hello world!"));
        results.add(JobResult.of("8a8d4e9fsf4f84", "456", "https://bbc.com", "There and back!"));
    }

    @Test
    public void whenFindById_shouldReturnRequest() {
        // given
        JobResult result = results.get(0);
        String id = entityManager.persistAndGetId(result).toString();
        entityManager.flush();

        // when
        JobResult actual = jobResultRepository.getOne(id);

        // then
        assertThat(id).isEqualTo(actual.getId());
    }

    @Test
    public void whenFindAll_shouldReturnAllRequests() {
        // given
        persistList(results);

        // when
        List<JobResult> actual = jobResultRepository.findAll();

        // then
        assertFalse(actual.isEmpty());
        assertThat(results.size()).isEqualTo(actual.size());
        assertNotNull(actual.get(actual.size() - 1));
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void whenDeleteById_shouldThrowException() {
        // given
        JobResult request = results.get(0);
        String id = entityManager.persistAndGetId(request).toString();
        entityManager.flush();

        // when
        jobResultRepository.deleteById(id);

        // then
        jobResultRepository.getOne(id);
    }

    @Test
    public void whenDeleteAll_shouldReturnZeroRequests() {
        // given
        persistList(results);

        // when
        jobResultRepository.deleteAll();

        // then
        List<JobResult> actual = jobResultRepository.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void whenUpdateRequest_shouldReturnUpdatedRequest() {
        // given
        JobResult request = results.get(0);
        JobResult saved = entityManager.merge(request);
        entityManager.flush();

        // then
        JobResult actual = jobResultRepository.getOne(saved.getId());
        assertNotNull(actual);
        assertThat(saved.getUrl()).isEqualTo(actual.getUrl());

        // when
        String newText = "A whole new world!!!";
        saved.setText(newText);
        entityManager.merge(saved);

        // then
        assertThat(saved.getUrl()).isEqualTo(actual.getUrl());
    }

    @After
    public void tearDown() throws Exception {
        entityManager.clear();
        results.clear();
    }
}
