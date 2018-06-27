package ee.bilal.dev.engine.webscraper.application.services;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by bilal90 on 5/4/2018.
 */
public interface ScrapperService {
    /**
     * Scrape websites
     * @param requests to perform
     * @param consumer handler of scrapping result
     */
    void scrape(List<JobRequestDTO> requests, Consumer<JobResultDTO> consumer);

    /**
     * Stop all jobs
     */
    void stopAll();
}
