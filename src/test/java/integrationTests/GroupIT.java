package integrationTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import config.Application;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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
public class GroupIT extends IntegrationTestTemplate {
    private static Long groupId;
    private static List<Long> idList = new ArrayList<>();

    @Test
    @Order(1)
    public void testCreateGroup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("title", "title1");
        prepareQuery("mutation ($title: String!) {createGroup(title: $title) {id}}", variables);
        ResponseEntity<String> response = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        groupId = Long.parseLong(jsonNode.get("data").get("createGroup").get("id").asText());
        assertTrue(jsonNode.get("data").get("createGroup").has("id"));
    }

    @Test
    @Order(2)
    public void testUpdateGroup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", groupId);
        variables.put("title", "title11");
        prepareQuery("mutation($id: Long!, $title: String!) {updateGroup(id: $id, title: $title) {id, title}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals("title11", jsonNode.get("data").get("updateGroup").get("title").asText());
    }

    @Test
    @Order(3)
    public void testDeleteGroup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", groupId);
        prepareQuery("mutation($id: Long!) {deleteGroup(id: $id)}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(groupId), jsonNode.get("data").get("deleteGroup").asText());
    }

    public void prepareQueryTestData() throws Exception {
        for (int i = 0;i < 10;i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode variables = objectMapper.createObjectNode();
            variables.put("title", "querytitle" + i);
            prepareQuery("mutation ($title: String!) {createGroup(title: $title) {id}}", variables);
            ResponseEntity<String> response = sendRequest();
            String actual = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(actual);
            idList.add(Long.parseLong(jsonNode.get("data").get("createGroup").get("id").asText()));
        }
    }

    @Test
    @Order(4)
    public void testGroup() throws Exception {
        prepareQueryTestData();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", idList.get(0));
        prepareQuery("query($id: Long!) {group(id: $id) {id, title}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(idList.get(0)), jsonNode.get("data").get("group").get("id").asText());
    }

    @Test
    @Order(5)
    public void testGroups() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("title", "querytitle");
        prepareQuery("query($title: String!) {groups(title: $title) {id, title}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(idList.size(), jsonNode.get("data").get("groups").size());
    }

    @Test
    @Order(6)
    public void testGroupByTitle() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("title", "querytitle1");
        prepareQuery("query($title: String!) {groupByTitle(title: $title) {id, title}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals("querytitle1", jsonNode.get("data").get("groupByTitle").get("title").asText());
    }
}
