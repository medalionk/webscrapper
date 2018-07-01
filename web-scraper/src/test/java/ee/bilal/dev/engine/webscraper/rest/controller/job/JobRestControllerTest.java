package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import ee.bilal.dev.engine.webscraper.util.IdFactory;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ee.bilal.dev.engine.webscraper.application.constants.Paths.JOBS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class JobRestControllerTest extends BaseControllerTest {
    private List<JobRequestDTO> jobRequests = new ArrayList<>();
    private List<JobReportDTO> jobReports = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        setDb();
    }

    @Test
    public void getAll_ValidCall_ReturnJobReports() throws Exception{
        //given(jobRestController.getAll()).willReturn(ResponseEntity.ok(jobReports));

        doGetThen(JOBS).statusCode(SC_OK);

        /*doGet(JOBS).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is(status.name())));*/
    }

    @Test
    public void getJob_ValidJobId_ReturnJobReport() throws Exception {
        JobReportDTO report = jobReports.get(0);
        //given(jobRestController.getJob(id)).willReturn(ResponseEntity.ok(report));

        /*doGet(JOBS + "/" + id).andExpect(status().isOk())
                .andExpect(jsonPath("$.frn", is(report.getFrn())));*/
    }

    @Test
    public void processJobs_ValidJobRequests_StartJobsAndReturnJobReports() throws Exception {
        String json = objectMapper.writeValueAsString(jobRequests);

        Response resultJson = doPost(JOBS, json);
        resultJson.then().statusCode(SC_OK);

        JsonPath reports = resultJson
                .then()
                .statusCode(SC_OK)
                .extract().jsonPath();


        //reports.
        //assertThat(retrievedBlogs..getInt("count")).isGreaterThan(7);
        //resultJson.jsonPath()"$[0].city", is(arrival.getCity())
        //List reports2 = reports.getList(new ArrayList<JobReportDTO>().getClass());
        ResponseBody body = resultJson.body();
        List<HashMap<String,String>> reports2 = resultJson.jsonPath().getList("");
        //resultJson.as(JobReportDTO[].class);
        assertEquals(reports2.size(), jobRequests.size());

        HashMap<String,String> report = reports2.get(0);

        assertNotNull(report);
        assertNotNull(report.get("id"));
        assertEquals(JobStatusDTO.CREATED.name(), report.get("status"));
    }

    @After
    public void tearDown() throws Exception {
    }

    private void setDb(){
        String url = "https://bbc.com";
        String frn = "1";
        String dateTimeStr = LocalDateTime.now().toString();
        String id = IdFactory.uuidID();
        JobStatusDTO status = JobStatusDTO.CREATED;

        float percentComplete = 0f;
        int maxLevel = 10;
        int linksPerLevel = 5;

        jobRequests.add(JobRequestDTO.of(url, frn, maxLevel, linksPerLevel));
        jobReports.add(JobReportDTO.of(id, dateTimeStr, frn, dateTimeStr, percentComplete, status));

    }
}