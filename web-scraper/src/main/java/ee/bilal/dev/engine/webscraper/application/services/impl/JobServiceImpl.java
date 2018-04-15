package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.application.services.JobService;
import ee.bilal.dev.engine.webscraper.domain.model.JobRequest;
import ee.bilal.dev.engine.webscraper.domain.repository.JobRequestRepository;
import ee.bilal.dev.engine.webscraper.util.StringUtil;
import ee.bilal.dev.engine.webscraper.util.UniqueSequence;
import ee.bilal.dev.engine.webscraper.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Observable;

import java.net.UnknownHostException;
import java.util.*;


///////
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class ListLinks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListLinks.class);
    private static float percentage = 0;

    public static void scrape(List<JobRequestDTO> reqs) {


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<String> future1 = executorService.submit(() -> "Hello World");
// some operations
        try {
            String result = future1.get();
            int f = 7;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long asyncStartTime = System.currentTimeMillis();
        List<Callable<Set<String>>> callableTasks = new ArrayList<>();
        for (JobRequestDTO req : reqs) {
            Consumer<JobResultDTO> consumer = (x) -> {
                x.setFrn(req.getFrn());
                print("\nResult: (%s)", x);
            };

            Set<String> nextLinks = new HashSet<>(Collections.singletonList(req.getUrl()));
            String id = UniqueSequence.getNext();
            callableTasks.add(() -> scraperWrapper(nextLinks, req.getLinksPerLevel(), req.getMaxLevel(), id, consumer));
        }

        List<String> asyncQueues = new ArrayList<>();
        try {
            List<Future<Set<String>>> futures = executorService.invokeAll(callableTasks);
            futures.forEach(x -> asyncQueues.addAll(waitResult(x)));
        } catch (InterruptedException e) {
            LOGGER.error("Execution intrrupted {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        long asyncEndTime = System.currentTimeMillis();


        long startTime = System.currentTimeMillis();
        List<String> syncQueues = new ArrayList<>();
        for (JobRequestDTO req : reqs) {
            LOGGER.info("Job request for: '{}' ", req);

            String id = UniqueSequence.getNext();
            Consumer<JobResultDTO> consumer = (x) -> {
                x.setId(id);
                x.setFrn(req.getFrn());
                print("\nResult: (%s)", x);
            };

            Set<String> nextLinks = new HashSet<>(Collections.singletonList(req.getUrl()));

            syncQueues.addAll(scraperWrapper(nextLinks, req.getLinksPerLevel(), req.getMaxLevel(), id, consumer));
        }

        long endTime = System.currentTimeMillis();

        print("\nAsynchronous\nQueued: (%d)\nElapsed Time: (%f)", asyncQueues.size(), (double)(asyncEndTime - asyncStartTime) / 1000.0);
        print("\nSynchronous\nQueued: (%d)\nElapsed Time: (%f)", syncQueues.size(), (double)(endTime - startTime) / 1000.0);
    }

    private static Set<String> waitResult(Future<Set<String>> future){
        try {
             return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Execution interrupted {}", e.getMessage());
            Thread.currentThread().interrupt();
        }

        return new HashSet<>();
    }

    private static Set<String> scraperWrapper(Set<String> urls, int linksPerLevel, int maxLevel, String id, Consumer<JobResultDTO> consumer){
        Set<String> queue = new HashSet<>();

        int initCurrentLevel = 0;
        scraper(urls, queue, linksPerLevel, maxLevel, initCurrentLevel, id, consumer);

        return queue;
    }

    private static void scraper(Set<String> urls, Set<String> queue, int linksPerLevel, int maxLevel, int currentLevel,
                                String id, Consumer<JobResultDTO> consumer){
        for (String url : urls) {
            if (currentLevel <= maxLevel){
                Set<String> nextUrls = new HashSet<>();
                List<String> currentUrls = scrapePage(url, consumer);
                nextUrls.addAll(getNextUrls(currentUrls, queue, linksPerLevel));
                queue.addAll(nextUrls);

                double approxTotal = Math.pow(linksPerLevel, maxLevel) + 1;
                percentage += (100 / approxTotal);
                scraper(nextUrls, queue, linksPerLevel, maxLevel, currentLevel + 1, id, consumer);
            }
        }
    }

    private static Set<String> getNextUrls(List<String> urls, Set<String> queue, int linksPerLevel){
        Set<String> nextUrls = new HashSet<>();

        for (int i = 0; nextUrls.size() < linksPerLevel && i < urls.size(); i++) {
            String url = urls.get(i);
            boolean queued = queue.contains(url);
            if(!queued) nextUrls.add(url);
        }

        return nextUrls;
    }

    private static Set<String> scrapePage2(Set<String> urls, int linksPerLevel, BiConsumer<String, String> consumer) {
        Set<String> nextUrls = new LinkedHashSet<>();
        Observable<String> observer = Observable.fromIterable(urls);
        observer.map(url -> {
            try {
                LOGGER.info("Current url to scrape: '{}' ", url);

                LOGGER.info("Connecting to '{}' ", url);
                Document doc = Jsoup.connect(url).get();

                LOGGER.info("Fetching text in '{}' ", url);
                String body = doc.body().text();

                LOGGER.info("Fetching links in '{}' ", url);
                Elements links = doc.select("a[href]");

                int linkSize = links.size();
                LOGGER.info("Total number of links in page: '{}' ", linkSize);

                for (int i = 0; i < linksPerLevel && i < linkSize; i++) {
                    nextUrls.add(links.get(i).attr("abs:href"));
                }

                return body;
            } catch (java.net.MalformedURLException e) {
                LOGGER.error("Malformed URL: '{}' ", url, e);
            } catch (UnknownHostException e) {
                LOGGER.error("Unknown host: '{}' ", url, e);
            } catch (IOException e) {
                LOGGER.error("IOException connecting to url: '{}' ", url, e);
            } catch (Exception e){
                LOGGER.error("Error connecting to url: '{}' ", url, e);
            }

            return null;
        });

        observer.subscribe(body -> consumer.accept(null, body));

        return nextUrls;
    }

    private static List<String> scrapePage(String url, Consumer<JobResultDTO> consumer) {
        LOGGER.info("Current url to scrape: '{}' ", url);
        ValidationUtil.validateStringNotNullOrEmpty(url, "url");

        try {
            LOGGER.info("Connecting to '{}' ", url);
            Document doc = Jsoup.connect(url).get();

            LOGGER.info("Fetching text in '{}' ", url);
            String body = doc.body().text();
            consumer.accept(JobResultDTO.of(url, body));

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

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}

@Service
public class JobServiceImpl implements JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    private final JobRequestRepository requestRepository;

    @Autowired
    public JobServiceImpl(JobRequestRepository requestRepository) {
        this.requestRepository = requestRepository;

        String url = "https://edition.cnn.com/world";
        String url2 = "https://bbc.com/";
        String url3 = "https://edition.cnn.com/world";
        String frn = "1";
        int level = 2;
        int linksPerLevel = 3;
        List<JobRequestDTO> requests = new ArrayList<>();
        JobRequestDTO request1 = JobRequestDTO.of(url, frn, level, linksPerLevel);
        JobRequestDTO request2 = JobRequestDTO.of(url2, frn, level, linksPerLevel);

        requests.add(request1);
        requests.add(request2);
        requests.add(request1);
        requests.add(request2);
        requests.add(request1);
        requests.add(request2);
        ListLinks.scrape(requests);
        int g = 6;
    }

    @Override
    public List<JobReportDTO> getAllJobs() {
        return new ArrayList<>();
    }

    @Override
    public JobReportDTO getJob(String jobId) {
        return null;
    }

    @Override
    public List<JobRequestDTO> processJobs(List<JobRequestDTO> requests) {
        return new ArrayList<>();
    }
}
