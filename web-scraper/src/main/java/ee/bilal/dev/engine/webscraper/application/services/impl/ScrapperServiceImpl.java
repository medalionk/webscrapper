package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobReportService;
import ee.bilal.dev.engine.webscraper.application.services.ScrapperService;
import ee.bilal.dev.engine.webscraper.configurations.ApplicationConfig;
import ee.bilal.dev.engine.webscraper.util.ValidationUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ScrapperServiceImpl implements ScrapperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperServiceImpl.class);

    private final JobReportService reportService;
    private final ApplicationConfig config;

    private ExecutorService executorService;

    @Autowired
    public ScrapperServiceImpl(JobReportService reportService, ApplicationConfig config) {
        this.reportService = reportService;
        this.config = config;
    }

    @Async
    @Override
    public void scrape(List<JobRequestDTO> reqs, Consumer<JobResultDTO> consumer) {
        initExecutorService();

        for (JobRequestDTO req : reqs) {
            LOGGER.info("Job request for: '{}' ", req);

            executorService.submit(() -> {
                Set<String> nextLinks = new HashSet<>(Collections.singletonList(req.getUrl()));
                scraper(nextLinks, req, consumer);

                reportService.markCompleted(req.getId());
            });
        }
    }

    /**
     * Scraper wrapper
     * @param urls set of urls
     * @param req Job request
     * @param consumer handler of scrapping result
     */
    private void scraper(Set<String> urls, JobRequestDTO req, Consumer<JobResultDTO> consumer){
        int initCurrentLevel = 0;
        scraper(urls, new HashSet<>(), initCurrentLevel, req, consumer);
    }

    @Override
    public void stopAll() {
        shutdownAndAwaitTermination(executorService);
    }

    /**
     * Scrape set of urls upto given level
     * @param urls set of urls
     * @param queue queued urls
     * @param currentLevel scrapper current level
     * @param req Job request
     * @param consumer handler of scrapping result
     */
    private void scraper(Set<String> urls, Set<String> queue, int currentLevel, JobRequestDTO req,
                         Consumer<JobResultDTO> consumer){
        for (String url : urls) {
            int linksPerLevel = req.getLinksPerLevel();
            int maxLevel = req.getMaxLevel();

            if (currentLevel <= maxLevel){
                LOGGER.info("Scrape page at '{}'", url);

                Set<String> nextUrls = new HashSet<>();
                req.setUrl(url);
                List<String> currentUrls = scrapePage(req, consumer);
                nextUrls.addAll(getNextUrls(currentUrls, queue, linksPerLevel));
                queue.addAll(nextUrls);

                double approxTotal = Math.pow(linksPerLevel, maxLevel) + 1;
                reportService.updateProgress(req.getId(), (float) (100 / approxTotal), JobStatusDTO.ONGOING);
                scraper(nextUrls, queue, currentLevel + 1, req, consumer);
            }
        }
    }

    /**
     * Only get urls that are not yet in queue
     * @param urls list of urls
     * @param queue urls already in queue
     * @param linksPerLevel number of urls to get per level
     * @return return set of urls not already in queue
     */
    private Set<String> getNextUrls(List<String> urls, Set<String> queue, int linksPerLevel){
        Set<String> nextUrls = new HashSet<>();

        for (int i = 0; nextUrls.size() < linksPerLevel && i < urls.size(); i++) {
            String url = urls.get(i);
            boolean isQueued = queue.contains(url);

            if(!isQueued) {
                nextUrls.add(url);
            }
        }

        return nextUrls;
    }

    /**
     *
     * @param req Job request
     * @param consumer callback to handle result
     * @return list of next urls
     */
    private static List<String> scrapePage(JobRequestDTO req, Consumer<JobResultDTO> consumer) {
        String url = req.getUrl();
        LOGGER.info("Handle JobRequest: '{}' ", req);
        ValidationUtil.validateStringNotNullOrEmpty(url, "url");

        try {
            LOGGER.info("Connecting to '{}' ", url);
            Document doc = Jsoup.connect(url).get();

            LOGGER.info("Fetching text in '{}' ", url);
            String body = doc.body().text();
            JobResultDTO result = JobResultDTO.of(req.getId(), req.getFrn(), url, body);
            consumer.accept(result);

            LOGGER.info("Fetching links in '{}' ", url);
            Elements links = doc.select("a[href]");

            int linkSize = links.size();
            LOGGER.info("Total number of links in page: '{}' ", linkSize);

            return links.stream().map(x -> x.attr("abs:href")).collect(Collectors.toList());
        } catch (java.net.MalformedURLException e) {
            LOGGER.error("Malformed URL: '{}' ", url, e);
        } catch (UnknownHostException e) {
            LOGGER.error("Unknown host: '{}' ", url, e);
        } catch (IOException e) {
            LOGGER.error("IOException connecting to url: '{}' ", url, e);
        } catch (Exception e){
            LOGGER.error("Error connecting to url: '{}' ", url, e);
        }

        return new ArrayList<>();
    }

    /**
     * Initialize executor service when new or after shutdown
     */
    private void initExecutorService(){
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newFixedThreadPool(config.getPoolSize());
        }
    }

    /**
     * Shutdown ongoing jobs
     * @param pool executor service pool
     */
    private void shutdownAndAwaitTermination(ExecutorService pool) {
        if(pool == null) return;

        final long timeout = 5;

        pool.shutdown(); // Disable new tasks from being submitted
        try {
            if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                pool.shutdownNow();

                if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                    LOGGER.error("Pool did not terminate");
                }
            }

            updateCanceledReports();
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Update reports to indicate cancellation
     */
    private void updateCanceledReports() {
        reportService
                .findAll()
                .stream()
                .filter(x -> x.getStatus() == JobStatusDTO.CREATED)
                .forEach(x -> reportService.updateStatus(x.getId(), JobStatusDTO.CANCELED));
    }
}
