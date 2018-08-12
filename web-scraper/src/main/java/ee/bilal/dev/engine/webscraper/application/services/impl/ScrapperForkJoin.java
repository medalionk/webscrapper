package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.util.ValidationUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ScrapperForkJoin {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapperForkJoin.class);
    private final Consumer<JobResultDTO> resultHandler;
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    /**
     * Instantiate Scrapper
     * @param resultHandler consumer to handle scrape result
     */
    public ScrapperForkJoin(final Consumer<JobResultDTO> resultHandler) {
        this.resultHandler = Objects.requireNonNull(resultHandler);
    }

    private class scrapePageAction extends RecursiveAction {
        private static final long serialVersionUID = -196522408291343951L;

        private final JobRequestDTO req;
        private final Runnable progressHandler;

        private final int currentLevel;

        /**
         * Create a ScrapePageAction
         * @param req JobRequest
         * @param progressHandler runnable to update progress
         * @param currentLevel of scrapping
         */
        scrapePageAction(final JobRequestDTO req, final Runnable progressHandler, int currentLevel) {
            this.req = Objects.requireNonNull(req);
            this.progressHandler = progressHandler;
            this.currentLevel = currentLevel;
        }

        @Override
        protected void compute() {
            // Return if maximum level is reached
            if(currentLevel >= req.getMaxLevel()){
                return;
            }

            LOGGER.info("Handle JobRequest: '{}' ", req);

            String url = req.getUrl();
            ValidationUtil.validateStringNotNullOrEmpty(url, "url");

            try {
                LOGGER.info("Connecting to '{}' ", url);
                Document doc = Jsoup.connect(url).get();

                LOGGER.info("Add URL '{}' to visited list ", url);
                //visitedUrls.add(url);

                LOGGER.info("Fetching text in '{}' ", url);
                String body = doc.body().text();
                JobResultDTO result = JobResultDTO.of(req.getId(), req.getFrn(), url, body);
                resultHandler.accept(result);

                synchronized(progressHandler)
                {
                    progressHandler.run();
                }
                //progressHandler.run();

                LOGGER.info("Fetching links in '{}' ", url);
                Elements innerLinks = doc.select("a[href]");

                int linkSize = innerLinks.size();
                LOGGER.info("Total number of links in page: '{}' ", linkSize);

                final List<scrapePageAction> actions = new ArrayList<>();
                List<String> links = innerLinks.stream()
                        .map(x -> x.attr("abs:href"))
                        .collect(Collectors.toList());

                final String jobId = req.getId();
                final String frn = req.getFrn();
                final int linksPerLevel = req.getLinksPerLevel();
                final int maxLevel = req.getMaxLevel();

                final Set<String> nextUrls = getNextUrls(links, linksPerLevel);
                if (!nextUrls.isEmpty()) {
                    for (final String nextUrl : nextUrls) {
                        final JobRequestDTO newReq = JobRequestDTO.of(nextUrl, frn, maxLevel, linksPerLevel);
                        newReq.setId(jobId);

                        final int nextLevel = currentLevel + 1;
                        final scrapePageAction action = new scrapePageAction(newReq, progressHandler, nextLevel);
                        action.fork();
                        actions.add(action);
                    }
                }

                actions.forEach(ForkJoinTask::join);

            } catch (java.net.MalformedURLException e) {
                LOGGER.error("Malformed URL: '{}' ", url, e);
            } catch (UnknownHostException e) {
                LOGGER.error("Unknown host: '{}' ", url, e);
            } catch (IOException e) {
                LOGGER.error("IOException connecting to url: '{}' ", url, e);
            } catch (Exception e){
                LOGGER.error("Error connecting to url: '{}' ", url, e);
            }
        }

        /**
         * Only get urls that have not yet been visited
         * @param urls list of urls
         * @param count number of urls to get per level
         * @return return set of urls
         */
        private Set<String> getNextUrls(final List<String> urls, final int count){
            final Set<String> nextUrls = new HashSet<>();

            for (int i = 0; nextUrls.size() < count && i < urls.size(); i++) {
                final String url = urls.get(i);

                if(!visitedUrls.contains(url)) {
                    visitedUrls.add(url);
                    nextUrls.add(url);
                }
            }

            return nextUrls;
        }
    }

    /**
     * Start scrapping pages
     * @param req Job request
     * @param progressHandler runnable to update progress
     */
    public void scrapePage(final JobRequestDTO req, final Runnable progressHandler) {
        final ForkJoinPool pool = new ForkJoinPool();
        final int initialLevel = -1;

        try {
            pool.invoke(new scrapePageAction(req, progressHandler, initialLevel));
        } finally {
            pool.shutdown();
        }
    }
}
