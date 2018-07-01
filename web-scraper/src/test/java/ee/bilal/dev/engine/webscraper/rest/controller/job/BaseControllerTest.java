package ee.bilal.dev.engine.webscraper.rest.controller.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Log;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Log
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource("classpath:test.properties")
public abstract class BaseControllerTest {
    private static final String BASE_HOST = "http://localhost";
    private static final String BASE_PATH = "/api/1.0";
    private static final String BASE_URL = "http://localhost/api/1.0";

    @LocalServerPort
    private int port;

    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = BASE_HOST;
        RestAssured.basePath = BASE_PATH;
        RestAssured.filters(singletonList(new ResponseLoggingFilter()));
        RestAssured.filters(singletonList(new RequestLoggingFilter()));
        RestAssured.port = port;

        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
    }

    ValidatableResponse doGetThen(String path) {
        return prepareGetWhen()
                .get(path)
                .then();
    }

    Response doPost(String path, String body) {
        return preparePostWhen(body)
                .post(path);
    }

    private RequestSpecification prepareGetWhen() {
        return given().when();
    }

    private RequestSpecification preparePostWhen(String body) {
        return given()
                .contentType(String.valueOf(APPLICATION_JSON))
                .body(body)
                .when();
    }
}
