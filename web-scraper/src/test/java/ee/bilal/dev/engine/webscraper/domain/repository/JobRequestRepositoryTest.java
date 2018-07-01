package ee.bilal.dev.engine.webscraper.domain.repository;

import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class JobRequestRepositoryTest extends BaseRepositoryTest{
    private List<JobRequest> requests = new ArrayList<>();

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Before
    public void setUp() {
        requests.add(JobRequest.of("https://bbc.com", "1", 10, 10));
        requests.add(JobRequest.of("https://cnn.com", "2", 7, 8));
    }

    @Test
    public void whenFindById_shouldReturnRequest() {
        // given
        JobRequest request = requests.get(0);
        String id = entityManager.persistAndGetId(request).toString();
        entityManager.flush();

        // when
        JobRequest actual = jobRequestRepository.getOne(id);

        // then
        assertThat(id).isEqualTo(actual.getId());
    }

    @Test
    public void whenFindAll_shouldReturnAllRequests() {
        // given
        persistList(requests);

        // when
        List<JobRequest> actual = jobRequestRepository.findAll();

        // then
        assertFalse(actual.isEmpty());
        assertThat(requests.size()).isEqualTo(actual.size());
        assertNotNull(actual.get(actual.size() - 1));
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void whenDeleteById_shouldThrowException() {
        // given
        JobRequest request = requests.get(0);
        String id = entityManager.persistAndGetId(request).toString();
        entityManager.flush();

        // when
        jobRequestRepository.deleteById(id);

        // then
        jobRequestRepository.getOne(id);
    }

    @Test
    public void whenDeleteAll_shouldReturnZeroRequests() {
        // given
        persistList(requests);

        // when
        jobRequestRepository.deleteAll();

        // then
        List<JobRequest> actual = jobRequestRepository.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void whenUpdateRequest_shouldReturnUpdatedRequest() {
        // given
        JobRequest request = requests.get(0);
        JobRequest saved = entityManager.merge(request);
        entityManager.flush();

        // then
        JobRequest actual = jobRequestRepository.getOne(saved.getId());
        assertNotNull(actual);
        assertThat(saved.getUrl()).isEqualTo(actual.getUrl());

        // when
        String newUrl = "https://stackoverflow.com/";
        saved.setUrl(newUrl);
        entityManager.merge(saved);

        // then
        assertThat(saved.getUrl()).isEqualTo(actual.getUrl());
    }

    @After
    public void tearDown() throws Exception {
        entityManager.clear();
        requests.clear();
    }
}
