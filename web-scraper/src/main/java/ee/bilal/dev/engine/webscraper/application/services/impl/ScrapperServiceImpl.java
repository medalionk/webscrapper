package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobReportService;
import ee.bilal.dev.engine.webscraper.application.services.ScrapperService;
import ee.bilal.dev.engine.webscraper.util.UniqueSequence;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class ScrapperServiceImpl implements ScrapperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperServiceImpl.class);
    private final JobReportService reportService;

    @Autowired
    public ScrapperServiceImpl(JobReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void scrapeAsync(List<JobRequestDTO> reqs, Consumer<JobResultDTO> consumer) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Set<String> asyncQueues = new HashSet<>();

        LOGGER.info("Create callable tasks.");
        List<Callable<Set<String>>> callableTasks = new ArrayList<>();
        for (JobRequestDTO req : reqs) {
            Set<String> nextLinks = new HashSet<>(Collections.singletonList(req.getUrl()));
            req.setId(UniqueSequence.getNext());
            callableTasks.add(() -> scraper(nextLinks, req, consumer));
        }

        LOGGER.info("Execute callable tasks.");
        try {
            List<Future<Set<String>>> futures = executorService.invokeAll(callableTasks);
            futures.forEach(x -> asyncQueues.addAll(waitResult(x)));
        } catch (InterruptedException e) {
            LOGGER.error("Execution intrrupted {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    //@Async
    @Override
    public void scrapeSync(List<JobRequestDTO> reqs, Consumer<JobResultDTO> consumer) {
        Set<String> syncQueues = new HashSet<>();
        for (JobRequestDTO req : reqs) {
            LOGGER.info("Job request for: '{}' ", req);

            req.setId(UniqueSequence.getNext());
            Set<String> nextLinks = new HashSet<>(Collections.singletonList(req.getUrl()));
            syncQueues.addAll(scraper(nextLinks, req, consumer));
        }
    }

    @Override
    public Set<String> scraper(Set<String> urls, JobRequestDTO req, Consumer<JobResultDTO> consumer){
        Set<String> queue = new HashSet<>();

        JobReportDTO report = createJobReport(req);
        int initCurrentLevel = 0;
        scraper(urls, queue, initCurrentLevel, req, consumer);

        return queue;
    }

    private JobReportDTO createJobReport(JobRequestDTO req){
        JobReportDTO report = new JobReportDTO();
        report.setId(req.getId());
        report.setFrn(req.getFrn());
        report.setDateTimeStarted(LocalDateTime.now().toString());
        report.setStatus(JobStatusDTO.STARTED);
        report.setPercentageComplete(0f);

        return reportService.create(report);
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
                reportService.updateProgress(req.getId(), (float) (100 / approxTotal));
                //percentage += (100 / approxTotal);
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
            if(!isQueued) nextUrls.add(url);
        }

        return nextUrls;
    }

    /**
     * Blocking wait for future results
     * @param future
     * @return
     */
    private Set<String> waitResult(Future<Set<String>> future){
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Execution interrupted {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return new HashSet<>();
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
}
