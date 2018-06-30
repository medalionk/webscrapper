package ee.bilal.dev.engine.webscraper.rest.controller.job;

import ee.bilal.dev.engine.webscraper.application.dtos.JobReportDTO;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static ee.bilal.dev.engine.webscraper.application.constants.Paths.JOBS;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JobRestControllerTest extends BaseTest{
    @Test
    public void getAll_ValidCall_ReturnJobReports() throws Exception{
        given(jobRestController.getAll()).willReturn(ResponseEntity.ok(jobReports));

        doGet(JOBS).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is(status.name())));
    }

    @Test
    public void getJob_ValidJobId_ReturnJobReport() throws Exception {
        JobReportDTO report = jobReports.get(0);
        given(jobRestController.getJob(id)).willReturn(ResponseEntity.ok(report));

        doGet(JOBS + "/" + id).andExpect(status().isOk())
                .andExpect(jsonPath("$.frn", is(report.getFrn())));
    }

    @Test
    public void processJobs_ValidJobRequests_StartJobsAndReturnJobReports() throws Exception {
        String json = objectMapper.writeValueAsString(jobRequests);

        given(jobRestController.processJobs(jobRequests)).willReturn(ResponseEntity.ok(jobReports));

        doPost(JOBS, json)
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
                .port(8080)
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(body)
                .when();
    }
}