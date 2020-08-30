package integrationTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import config.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIT extends IntegrationTestTemplate {
    private static Long userId;
    private static List<Long> idList = new ArrayList<>();

    @Test
    @Order(1)
    public void testCreateUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("username", "user1");
        prepareQuery("mutation ($username: String!) {createUser(username: $username) {id}}", variables);
        ResponseEntity<String> response = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        userId = Long.parseLong(jsonNode.get("data").get("createUser").get("id").asText());
        assertTrue(jsonNode.get("data").get("createUser").has("id"));
    }

    @Test
    @Order(2)
    public void testUpdateUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", userId);
        variables.put("username", "user11");
        prepareQuery("mutation($id: Long, $username: String!) {updateUser(id: $id, username: $username) {id, username}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals("user11", jsonNode.get("data").get("updateUser").get("username").asText());
    }

    @Test
    @Order(3)
    public void testDeleteUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", userId);
        prepareQuery("mutation($id: Long!) {deleteUser(id: $id)}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(userId), jsonNode.get("data").get("deleteUser").asText());
    }

    public void prepareQueryTestData() throws Exception {
        for (int i = 0;i < 10;i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode variables = objectMapper.createObjectNode();
            variables.put("username", "user" + i);
            prepareQuery("mutation ($username: String!) {createUser(username: $username) {id}}", variables);
            ResponseEntity<String> response = sendRequest();
            String actual = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(actual);
            idList.add(Long.parseLong(jsonNode.get("data").get("createUser").get("id").asText()));
        }
    }

    @Test
    @Order(4)
    public void testUser() throws Exception {
        prepareQueryTestData();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", idList.get(0));
        prepareQuery("query($id: Long!) {user(id: $id) {id, username}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(idList.get(0)), jsonNode.get("data").get("user").get("id").asText());
    }

    @Test
    @Order(5)
    public void testUsers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("username", "user");
        prepareQuery("query($username: String!) {users(username: $username) {id, username}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(idList.size(), jsonNode.get("data").get("users").size());
    }

    @Test
    @Order(6)
    public void testUserByUsername() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("username", "user1");
        prepareQuery("query($username: String!) {userByUsername(username: $username) {id, username}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals("user1", jsonNode.get("data").get("userByUsername").get("username").asText());
    }
}
