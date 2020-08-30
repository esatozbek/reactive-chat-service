package integrationTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import config.Application;
import enums.MessageStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageIT extends IntegrationTestTemplate {
    private static Long messageId;
    private static List<Long> idList = new ArrayList<>();
    private static List<Long> userIdList = new ArrayList<>();
    private static List<Long> groupIdList = new ArrayList<>();

    public void prepareTestData() throws Exception {
        for (int i = 0;i < 5;i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode variables = objectMapper.createObjectNode();
            variables.put("username", "user" + i);
            prepareQuery("mutation ($username: String!) {createUser(username: $username) {id}}", variables);
            ResponseEntity<String> response = sendRequest();
            String actual = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(actual);
            userIdList.add(Long.parseLong(jsonNode.get("data").get("createUser").get("id").asText()));

            variables = objectMapper.createObjectNode();
            variables.put("title", "title" + i);
            prepareQuery("mutation ($title: String!) {createGroup(title: $title) {id}}", variables);
            ResponseEntity<String> responseGroup = sendRequest();
            JsonNode parsedGroup = objectMapper.readTree(responseGroup.getBody());
            groupIdList.add(Long.parseLong(parsedGroup.get("data").get("createGroup").get("id").asText()));
        }
    }

    @Test
    @Order(1)
    public void testCreateMessage() throws Exception {
        prepareTestData();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("content", "dummy content");
        variables.put("status", MessageStatus.RECEIVED.name());
        variables.put("timestamp", System.currentTimeMillis());
        variables.put("senderId", userIdList.get(0));
        variables.put("receiverId", userIdList.get(1));
        prepareQuery("mutation($content: String!, $status: MessageStatus!, $timestamp: Long!, $senderId: Long!, $receiverId: Long!) " +
                "{createMessage(content: $content, status: $status, timestamp: $timestamp, senderId: $senderId, receiverId: $receiverId) {id}}", variables);
        ResponseEntity<String> response = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        messageId = Long.parseLong(jsonNode.get("data").get("createMessage").get("id").asText());
        assertTrue(jsonNode.get("data").get("createMessage").has("id"));
    }

    @Test
    @Order(2)
    public void testUpdateMessage() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", messageId);
        variables.put("content", "content2");
        variables.put("status", MessageStatus.SENT.name());
        variables.put("timestamp", System.currentTimeMillis());
        prepareQuery("mutation($id: Long!, $content: String!, $status: MessageStatus!) " +
                "{updateMessage(id: $id, content: $content, status: $status) {id, content}}", variables);
        ResponseEntity<String> response = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertEquals(messageId, jsonNode.get("data").get("updateMessage").get("id").asLong());
        assertEquals("content2", jsonNode.get("data").get("updateMessage").get("content").asText());
    }

    @Test
    @Order(3)
    public void testDeleteMessage() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", messageId);
        prepareQuery("mutation($id: Long!) {deleteMessage(id: $id)}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(messageId), jsonNode.get("data").get("deleteMessage").asText());
    }

    public void prepareQueryTestData() throws Exception {
        Random r = new Random();
        for (int i = 0;i < 10;i++) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode variables = objectMapper.createObjectNode();
            variables.put("content", "dummy content" + " " + i);
            variables.put("status", MessageStatus.RECEIVED.name());
            variables.put("timestamp", System.currentTimeMillis());
            int id1 = r.nextInt(4);
            variables.put("senderId", userIdList.get(id1));
            int id2 = r.nextInt(9);
            while (id1 == id2 || id2 - 5 == id1) id2 = r.nextInt(9);
            if (id2 <= 4) {
                variables.put("receiverId", userIdList.get(id2));
            } else {
                variables.put("groupId", groupIdList.get(id2 - 5));
            }
            prepareQuery("mutation($content: String!, $status: MessageStatus!, $timestamp: Long!, $senderId: Long!, $receiverId: Long, $groupId: Long) " +
                    "{createMessage(content: $content, status: $status, timestamp: $timestamp, senderId: $senderId, receiverId: $receiverId, groupId: $groupId) {id}}", variables);
            ResponseEntity<String> response = sendRequest();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            idList.add(Long.parseLong(jsonNode.get("data").get("createMessage").get("id").asText()));
        }
    }

    @Test
    @Order(4)
    public void testMessage() throws Exception {
        prepareQueryTestData();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("id", idList.get(0));
        prepareQuery("query($id: Long!) {message(id: $id) {id, content, sender{id, username}, status, receiver{id, username}, group{id, title}}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(String.valueOf(idList.get(0)), jsonNode.get("data").get("message").get("id").asText());
    }

    @Test
    @Order(5)
    public void testMessages() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode variables = objectMapper.createObjectNode();
        variables.put("content", "content");
        prepareQuery("query($content: String) {messages(content: $content) {id, content, status, timestamp}}", variables);
        ResponseEntity<String> responseEntity = sendRequest();
        JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
        assertEquals(idList.size(), jsonNode.get("data").get("messages").size());
    }
}
