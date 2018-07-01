package ee.bilal.dev.engine.webscraper.domain.repository;

import ee.bilal.dev.engine.webscraper.domain.model.JobReport;
import ee.bilal.dev.engine.webscraper.domain.model.JobStatus;
import ee.bilal.dev.engine.webscraper.util.IdFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class JobReportRepositoryTest extends BaseRepositoryTest{
    private List<JobReport> reports = new ArrayList<>();

    @Autowired
    private JobReportRepository jobReportRepository;

    @Before
    public void setUp() {
        reports.add(JobReport.of(IdFactory.uuidID(), LocalDateTime.now().toString(), "1234",
                null, 0.0f, JobStatus.COMPLETED));
        reports.add(JobReport.of(IdFactory.uuidID(), LocalDateTime.now().toString(), "456",
                null, 0.0f, JobStatus.ONGOING));
    }

    @Test
    public void whenFindById_shouldReturnRequest() {
        // given
        JobReport request = reports.get(0);
        String id = entityManager.persistAndGetId(request).toString();
        entityManager.flush();

        // when
        JobReport actual = jobReportRepository.getOne(id);

        // then
        assertThat(id).isEqualTo(actual.getId());
    }

    @Test
    public void whenFindAll_shouldReturnAllRequests() {
        // given
        persistList(reports);

        // when
        List<JobReport> actual = jobReportRepository.findAll();

        // then
        assertFalse(actual.isEmpty());
        assertThat(reports.size()).isEqualTo(actual.size());
        assertNotNull(actual.get(actual.size() - 1));
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void whenDeleteById_shouldThrowException() {
        // given
        JobReport report = reports.get(0);
        String id = entityManager.persistAndGetId(report).toString();
        entityManager.flush();

        // when
        jobReportRepository.deleteById(id);

        // then
        jobReportRepository.getOne(id);
    }

    @Test
    public void whenDeleteAll_shouldReturnZeroRequests() {
        // given
        persistList(reports);

        // when
        jobReportRepository.deleteAll();

        // then
        List<JobReport> actual = jobReportRepository.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void whenUpdateRequest_shouldReturnUpdatedRequest() {
        // given
        JobReport request = reports.get(0);
        JobReport saved = entityManager.merge(request);
        entityManager.flush();

        // then
        JobReport actual = jobReportRepository.getOne(saved.getId());
        assertNotNull(actual);
        assertThat(saved.getFrn()).isEqualTo(actual.getFrn());

        // when
        saved.setStatus(JobStatus.ONGOING);
        entityManager.merge(saved);

        // then
        assertThat(saved.getFrn()).isEqualTo(actual.getFrn());
    }

    @After
    public void tearDown() throws Exception {
        entityManager.clear();
        reports.clear();
    }
}
