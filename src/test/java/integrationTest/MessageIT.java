package integrationTest;

import config.Application;
import dto.MessageDTO;
import enums.MessageStatus;
import integrationTest.helpers.TestData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import request.MessageRequest;
import response.IdResponse;

@SpringBootTest(classes = { Application.class, TestData.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageIT {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    TestData testData;

    private static final String MESSAGE_URI = "/message";

    private static Long messageId;

    @Test
    @Order(1)
    public void createMessageTest() {
        MessageRequest request = TestData.prepareMessageRequest(
                testData.getUserIdList().get(0),
                testData.getUserIdList().get(0)
        );

        IdResponse response = webTestClient
                .post()
                .uri(MESSAGE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertTrue(response.getStatus());
        messageId = response.getId();
    }

    @Test
    @Order(2)
    public void getMessageTest() {
        webTestClient
                .get()
                .uri(MESSAGE_URI + "/" + messageId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageDTO.class)
                .consumeWith(response -> {
                    Assertions.assertEquals(messageId, response.getResponseBody().getId());
                });
    }

    @Test
    @Order(3)
    public void updateMessageTest() {
        MessageRequest request = TestData.prepareMessageRequest(
                testData.getUserIdList().get(0),
                testData.getUserIdList().get(0)
        );
        request.setContent("content2");
        request.setStatus(MessageStatus.RECEIVED);

        webTestClient
                .put()
                .uri(MESSAGE_URI + "/" + messageId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }

    @Test
    @Order(4)
    public void deleteMessageTest() {
        webTestClient
                .delete()
                .uri(MESSAGE_URI + "/" + messageId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }

    @Test
    @Order(5)
    public void getMessagesTest() {
        testData.prepareMessageTestData(webTestClient);

        webTestClient
                .get()
                .uri(MESSAGE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class)
                .hasSize(testData.getMessageIdList().size());
    }

    @Test
    @Order(6)
    public void getMessagesBySenderTest() {
        webTestClient
                .get()
                .uri(MESSAGE_URI + "/sender/" + testData.getUserIdList().get(0))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class)
                .hasSize(testData.getMessageIdList().size());
    }

    @Test
    @Order(7)
    public void getMessagesByReceiverTest() {
        webTestClient
                .get()
                .uri(MESSAGE_URI + "/receiver/" + testData.getUserIdList().get(0))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class)
                .hasSize(testData.getMessageIdList().size());
    }

    @Test
    @Order(8)
    public void getMessagesByGroupTest() {
        webTestClient
                .get()
                .uri(MESSAGE_URI + "/group/" + testData.getGroupIdList().get(0))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class);
    }

    @Test
    @Order(9)
    public void getMessagesByContentTest() {
        webTestClient
                .get()
                .uri(MESSAGE_URI + "/content/content")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class);
    }

    @Test
    @Order(10)
    public void getMessagesByTimestampsTest() {
        Long start = System.currentTimeMillis() - 60 * 60 * 1000;
        Long end = System.currentTimeMillis();

        webTestClient
                .get()
                .uri(MESSAGE_URI + "/start/" + start + "/end/" + end)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MessageDTO.class);
    }
}
