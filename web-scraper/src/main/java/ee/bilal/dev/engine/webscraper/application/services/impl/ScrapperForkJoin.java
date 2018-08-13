package ee.bilal.dev.engine.webscraper.application.services.impl;

import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobResultDTO;
import ee.bilal.dev.engine.webscraper.util.UrlUtil;
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
    private static final String[] schemes = {"http","https"};

    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();

    private final Consumer<JobResultDTO> resultHandler;
    private final JobRequestDTO req;
    private final Runnable progressHandler;

    /**
     * Instantiate Scrapper
     * @param resultHandler consumer to handle scrape result
     */
    public ScrapperForkJoin(final JobRequestDTO req, final Consumer<JobResultDTO> resultHandler,
                            final Runnable progressHandler) {
        validateJobRequest(req);

        this.req = req;
        this.resultHandler = Objects.requireNonNull(resultHandler);
        this.progressHandler = Objects.requireNonNull(progressHandler);
    }

    private class scrapePageAction extends RecursiveAction {
        private static final long serialVersionUID = -196522408291343951L;

        private final String url;
        private final int currentLevel;

        /**
         * Create a ScrapePageAction
         * @param url of current page to scrape
         * @param currentLevel of scrapping
         */
        scrapePageAction(final String url, final int currentLevel) {
            this.url = url;
            this.currentLevel = currentLevel;
        }

        @Override
        protected void compute() {
            // Return if maximum level is reached
            if(currentLevel >= req.getMaxLevel()){
                return;
            }

            try {
                LOGGER.info("Connecting to '{}' ", url);
                Document doc = Jsoup.connect(url).get();

                LOGGER.info("Fetching text in '{}' ", url);
                String body = doc.body().text();
                JobResultDTO result = JobResultDTO.of(req.getId(), req.getFrn(), url, body);
                resultHandler.accept(result);

                synchronized(progressHandler)
                {
                    progressHandler.run();
                }

                LOGGER.info("Fetching links in '{}' ", url);
                Elements innerLinks = doc.select("a[href~=^[^#]+$]");

                int linkSize = innerLinks.size();
                LOGGER.info("Total number of links in page: '{}' ", linkSize);

                final List<scrapePageAction> actions = new ArrayList<>();
                List<String> links = innerLinks.stream()
                        .map(x -> x.attr("abs:href"))
                        .collect(Collectors.toList());

                final int linksPerLevel = req.getLinksPerLevel();
                final Set<String> nextUrls = getNextUrls(links, linksPerLevel);

                if (!nextUrls.isEmpty()) {
                    for (final String nextUrl : nextUrls) {
                        final int nextLevel = currentLevel + 1;
                        final scrapePageAction action = new scrapePageAction(nextUrl, nextLevel);

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
                if(!UrlUtil.isValidUrl(url, schemes))
                    continue;

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
     */
    public void scrapePage() {
        LOGGER.info("Handle JobRequest: '{}' ", req);

        final ForkJoinPool pool = new ForkJoinPool();
        final int initialLevel = -1;

        try {
            pool.invoke(new scrapePageAction(req.getUrl(), initialLevel));
        } finally {
            pool.shutdown();
        }
    }

    private void validateJobRequest(final JobRequestDTO req){
        final String url = req.getUrl();

        ValidationUtil.validateStringNotNullOrEmpty(url, "url");
        ValidationUtil.validateStringNotNullOrEmpty(req.getFrn(), "frn");
        ValidationUtil.validateStringNotNullOrEmpty(url, "url");

        if(!UrlUtil.isValidUrl(url, schemes)){
            throw new IllegalArgumentException(String.format("URL '%s' is not valid!!!", url));
        }

        if(req.getLinksPerLevel() < 1){
            throw new IllegalArgumentException("LinksPerLevel cannot be less than 1!!!");
        }

        if(req.getMaxLevel() < 1){
            throw new IllegalArgumentException("MaxLevel cannot be less than 1!!!");
        }
    }
}
