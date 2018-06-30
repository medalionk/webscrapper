package ee.bilal.dev.engine.webscraper.rest.controller.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.bilal.dev.engine.webscraper.application.constants.AppGlobal;
import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.util.IdFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static ee.bilal.dev.engine.webscraper.application.constants.Paths.JOBS;
import static ee.bilal.dev.engine.webscraper.application.constants.Paths.VERSION;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:test.properties")
public abstract class BaseTest {
    protected static final String url = "https://bbc.com";
    protected static final String frn = "1";
    protected static final String dateTimeStr = LocalDateTime.now().toString();
    protected static final String id = IdFactory.uuidID();

    protected static final JobStatusDTO status = JobStatusDTO.CREATED;

    protected float percentComplete = 0f;
    protected int maxLevel = 10;
    protected int linksPerLevel = 5;

    protected ObjectMapper objectMapper;

    protected List<JobRequestDTO> jobRequests;
    protected List<JobReportDTO> jobReports;

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected JobRestController jobRestController;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();

        JobRequestDTO jobRequest = JobRequestDTO.of(url, frn, maxLevel, linksPerLevel);
        jobRequests = singletonList(jobRequest);

        JobReportDTO jobReport = JobReportDTO.of(id, dateTimeStr, frn, dateTimeStr, percentComplete, status);
        jobReports = singletonList(jobReport);
    }

    @After
    public void tearDown() throws Exception {
    }

    protected ResultActions doGet(String path) throws Exception{
        return mvc.perform(MockMvcRequestBuilders.get(VERSION + path)
                .contentType(APPLICATION_JSON));
    }

    protected ResultActions doPost(String path, String body) throws Exception{
        return mvc.perform(MockMvcRequestBuilders.post(VERSION + JOBS)
                .contentType(APPLICATION_JSON)
                .content(body).characterEncoding(AppGlobal.UTF8));
    }
}
