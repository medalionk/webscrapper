package ee.bilal.dev.engine.webscraper.domain.repository;

import ee.bilal.dev.engine.webscraper.domain.model.JobResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobResultRepository extends JpaRepository<JobResult, String> {

}
