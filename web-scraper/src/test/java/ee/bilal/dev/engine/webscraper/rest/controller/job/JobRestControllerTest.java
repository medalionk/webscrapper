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
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ee.bilal.dev.engine.webscraper.application.constants.Paths.JOBS;
import static ee.bilal.dev.engine.webscraper.application.constants.Paths.REPORT;
import static ee.bilal.dev.engine.webscraper.application.constants.Paths.STOP_ALL;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_METHOD_NOT_ALLOWED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;


public class JobRestControllerTest extends BaseControllerTest {
    private static List<JobRequestDTO> jobRequests = new ArrayList<>();
    private static List<JobReportDTO> jobReports = new ArrayList<>();

    @BeforeClass
    public static void init() {
        setDb();
    }

    @Test
    public void whenInValidEndpoint_shouldReturnNotFoundHttpCode() throws Exception{
        prepareGetWhen()
                .get("/some-endpoint")
                .then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void getAll_whenValidCall_shouldReturnOkHttpCode() throws Exception{
        prepareGetWhen()
                .get(JOBS)
                .then().statusCode(SC_OK);
    }

    @Test
    public void getAll_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws Exception{
        prepareGetWhen()
                .delete(JOBS)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJob_whenInValidParams_shouldThrowIllegalArgumentException() throws Exception{
        prepareGetWhen()
                .get(JOBS + "/{}");
    }

    @Test
    public void getJob_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws Exception{
        prepareGetWhen()
                .delete(JOBS + "/some-id")
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getReport_whenValidCall_shouldReturnOkHttpCode() throws Exception{
        prepareGetWhen()
                .get(REPORT)
                .then().statusCode(SC_OK);
    }

    @Test
    public void getReport_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws Exception{
        prepareGetWhen()
                .delete(REPORT)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void stopAll_whenValidCall_shouldReturnOkHttpCode() throws Exception{
        prepareGetWhen()
                .get(STOP_ALL)
                .then().statusCode(SC_OK);
    }

    @Test
    public void stopAll_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws Exception{
        prepareGetWhen()
                .delete(STOP_ALL)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void processJobs_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws Exception{
        String json = objectMapper.writeValueAsString(jobRequests);

        given()
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(json)
                .when()
                .put(JOBS)
                .then()
                .statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void processJobs_whenInvalidPostEntity_shouldReturnBadRequestHttpCode() throws Exception{
        String json = "Invalid json object";

        given()
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(json)
                .when()
                .post(JOBS)
                .then()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void processJobs_ValidJobRequests_StartJobsAndReturnJobReports() throws Exception {
        String json = objectMapper.writeValueAsString(jobRequests);

        Response resultJson = doPost(JOBS, json);
        resultJson.then().statusCode(SC_OK);

        List<HashMap<String,String>> reports = resultJson
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getList("");

        assertEquals(jobRequests.size(), reports.size());

        HashMap<String,String> report = reports.get(0);

        assertNotNull(report);
        assertNotNull(report.get("id"));
        assertEquals(JobStatusDTO.CREATED.name(), report.get("status"));
    }

    private static void setDb(){
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