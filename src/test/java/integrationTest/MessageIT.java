package integrationTest;

import config.Application;
import dto.MessageDTO;
import dto.UserDTO;
import enums.MessageStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import response.IdResponse;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageIT {
    @Autowired
    WebTestClient webTestClient;

    private static Long messageId;
    private static Long senderId;
    private static Long receiverId;


    public void before() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("sender");
        IdResponse response = webTestClient
                .post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .returnResult()
                .getResponseBody();

        senderId = response.getId();

        userDTO.setUsername("receiver");
        IdResponse response2 = webTestClient
                .post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .returnResult()
                .getResponseBody();

        receiverId = response2.getId();
    }

    @Test
    @Order(1)
    public void createMessageTest() {
        before();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("content");
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTO.setStatus(MessageStatus.SENT);
        messageDTO.setSenderId(senderId);
        messageDTO.setReceiverId(receiverId);

        IdResponse response = webTestClient
                .post()
                .uri("/message")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(messageDTO)
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
                .uri("/message/" + messageId)
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
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("content");
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTO.setStatus(MessageStatus.SENT);
        messageDTO.setSenderId(senderId);
        messageDTO.setReceiverId(receiverId);

        webTestClient
                .put()
                .uri("/message/" + messageId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(messageDTO)
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
                .uri("/message/" + messageId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }
}
