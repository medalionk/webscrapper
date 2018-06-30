package ee.bilal.dev.engine.webscraper.rest.controller.job;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.configurations.ApplicationConfig;
import ee.bilal.dev.engine.webscraper.util.IdFactory;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;


@RunWith(SpringRunner.class)
//@SpringBootTest(classes=ApplicationConfig.class)
@SpringBootTest
//@WebMvcTest(JobRestController.class)
@AutoConfigureMockMvc
//@ActiveProfiles("test")
@TestPropertySource("classpath:test.properties")
public class JobRestControllerTest {
    private static final String PATH = "http://127.0.0.1:8080/api/";
    @Autowired(required=true)
    private MockMvc mvc;

//    @Autowired(required=false)
//    private BindingResult result;

    @Autowired(required=false)
    @Qualifier("objectMapper")
    private ObjectMapper mapper;

    //@LocalServerPort
    private int port;

    @MockBean
    private JobRestController jobRestController;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAll() throws Exception {
    }

    @Test
    public void getJob() throws Exception {
    }

    @Test
    public void process() throws Exception {
        String url = "https://bbc.com";
        String frn = "1";
        String dateTimeStr = LocalDateTime.now().toString();
        String id = IdFactory.uuidID();

        JobStatusDTO status = JobStatusDTO.CREATED;

        float percentComplete = 0f;
        int maxLevel = 10;
        int linksPerLevel = 5;

        JobRequestDTO jobRequest = JobRequestDTO.of(url, frn, maxLevel, linksPerLevel);
        List<JobRequestDTO> jobRequests = singletonList(jobRequest);

        JobReportDTO jobReport = JobReportDTO.of(id, dateTimeStr, frn, dateTimeStr, percentComplete, status);
        List<JobReportDTO> jobReports = singletonList(jobReport);

        String json = getObjectMapper().writeValueAsString(jobRequests);

        BDDMockito.given(jobRestController.processJobs(jobRequests)).willReturn(ResponseEntity.ok(jobReports));

        mvc.perform(MockMvcRequestBuilders.post(PATH + "jobs")
                .contentType(APPLICATION_JSON)
                .content(json).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is(status.name())));
    }

    @Test
    public void getStatus() throws Exception {
    }

    @Test
    public void stopOngoingJobs() throws Exception {
    }

    private RequestSpecification preparePostPutWhen(String body) {
        return io.restassured.RestAssured.given()
                .port(port)
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(body)
                .when();
    }

    ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .registerModules(new JavaTimeModule());
    }
}