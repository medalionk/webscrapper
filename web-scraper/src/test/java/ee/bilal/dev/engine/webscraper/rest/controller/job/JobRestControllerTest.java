package ee.bilal.dev.engine.webscraper.rest.controller.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import ee.bilal.dev.engine.webscraper.application.dtos.JobRequestDTO;
import ee.bilal.dev.engine.webscraper.application.dtos.JobStatusDTO;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ee.bilal.dev.engine.webscraper.application.constants.Paths.*;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


public class JobRestControllerTest extends BaseControllerTest {
    private static List<JobRequestDTO> jobRequests = new ArrayList<>();

    @BeforeClass
    public static void init() {
        setDb();
    }

    @Test
    public void whenInValidEndpoint_shouldReturnNotFoundHttpCode() {
        prepareGetWhen()
                .get("/some-endpoint")
                .then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void getAll_whenValidCall_shouldReturnOkHttpCode() {
        prepareGetWhen()
                .get(JOBS)
                .then().statusCode(SC_OK);
    }

    @Test
    public void getAll_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() {
        prepareGetWhen()
                .delete(JOBS)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getJob_whenInValidParams_shouldThrowIllegalArgumentException() {
        prepareGetWhen()
                .get(JOBS + "/{}");
    }

    @Test
    public void getJob_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() {
        prepareGetWhen()
                .delete(JOBS + "/some-id")
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getJob_whenGetJobById_shouldReturnJobReport() throws JsonProcessingException {
        List<HashMap<String,String>> reports = submitJobs(jobRequests);

        assertNotNull(reports);
        assertFalse(reports.isEmpty());

        String id = reports.get(0).get("id");

        HashMap<String,String> report = prepareGetWhen()
                .get(JOBS + "/" + id)
                .then().statusCode(SC_OK).extract().jsonPath().get("");

        assertNotNull(report);
        assertEquals(id, report.get("id"));
        assertEquals(JobStatusDTO.CREATED.name(), report.get("status"));
    }

    @Test
    public void getReport_whenValidCall_shouldReturnOkHttpCode() {
        prepareGetWhen()
                .get(REPORT)
                .then().statusCode(SC_OK);
    }

    @Test
    public void getReport_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() {
        prepareGetWhen()
                .delete(REPORT)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void getReport_whenJobsStarted_shouldReturnJobsReport() throws JsonProcessingException {
        List<HashMap<String,String>> reports = submitJobs(jobRequests);

        assertNotNull(reports);
        assertFalse(reports.isEmpty());

        HashMap<String,String> report = prepareGetWhen()
                .get(REPORT)
                .then().statusCode(SC_OK).extract().jsonPath().get("");

        assertNotNull(report);
        assertNotNull(report.get("totalJobs"));
        assertNotNull(report.get("totalCompleted"));
        assertNotNull(report.get("totalOngoing"));
        assertNotNull(report.get("totalCanceled"));
        assertNotNull(report.get("percentCompleted"));
    }

    @Test
    public void stopAll_whenValidCall_shouldReturnOkHttpCode() {
        prepareGetWhen()
                .get(STOP_ALL)
                .then().statusCode(SC_OK);
    }

    @Test
    public void stopAll_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() {
        prepareGetWhen()
                .delete(STOP_ALL)
                .then().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Test
    public void stopAll_whenJobsStartedStopAll_shouldReturnOkHttpCode() throws JsonProcessingException {
        List<HashMap<String,String>> reports = submitJobs(jobRequests);

        assertNotNull(reports);
        assertFalse(reports.isEmpty());

        prepareGetWhen()
                .get(STOP_ALL)
                .then().statusCode(SC_OK);
    }

    @Test
    public void processJobs_whenInvalidMethod_shouldReturnMethodNotAllowedHttpCode() throws JsonProcessingException{
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
    public void processJobs_whenInvalidPostEntity_shouldReturnBadRequestHttpCode() {
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
    public void processJobs_ValidJobRequests_StartJobsAndReturnJobReports() throws JsonProcessingException {
        List<HashMap<String,String>> actual = submitJobs(jobRequests);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(jobRequests.size(), actual.size());

        HashMap<String,String> report = actual.get(0);

        assertNotNull(report);
        assertNotNull(report.get("id"));
        assertEquals(JobStatusDTO.CREATED.name(), report.get("status"));
    }

    @After
    public void tearDown() {
        doGetThen(STOP_ALL);
    }

    private List<HashMap<String,String>> submitJobs(List<JobRequestDTO> requests) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(requests);

        Response resultJson = doPost(JOBS, json);

        return resultJson
                .then()
                .statusCode(SC_CREATED)
                .extract()
                .jsonPath()
                .getList("");
    }

    private static void setDb(){
        jobRequests.add(JobRequestDTO.of("https://bbc.com", "1", 10, 5));
    }
}