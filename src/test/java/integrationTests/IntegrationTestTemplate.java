package integrationTests;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public abstract class IntegrationTestTemplate {
    @LocalServerPort
    private int port;
    private String endpoint;
    private JsonNode queryWrapper;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private JsonStringEncoder jsonStringEncoder = JsonStringEncoder.getInstance();

    @BeforeEach
    public void prepareTests() {
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
        endpoint = "http://localhost:" + port + "/graphql";
        queryWrapper = new ObjectMapper().createObjectNode();
    }

    @AfterEach
    public void afterTests() {
        queryWrapper = new ObjectMapper().createObjectNode();
    }

    public void prepareQuery(String query, ObjectNode variables) {
        ((ObjectNode) queryWrapper).put("query", escapeQuery(query));
        ((ObjectNode) queryWrapper).set("variables", variables);
    }

    public String escapeQuery(String query) {
        return new String(jsonStringEncoder.quoteAsString(query));
    }

    public ResponseEntity<String> sendRequest() {
        HttpEntity<String> entity = new HttpEntity<>(createJsonQuery(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
        System.out.println(responseEntity.getBody());
        return responseEntity;
    }

    public String createJsonQuery() {
        return queryWrapper.toString();
    }
}
