package integrationTest;

import config.Application;
import dto.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import response.IdResponse;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIT {
    @Autowired
    WebTestClient webTestClient;

    private static Long userId;

    @Test
    @Order(1)
    public void createUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user1");
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

        Assertions.assertTrue(response.getStatus());
        userId = response.getId();
    }

    @Test
    @Order(2)
    public void getUserTest() {
        webTestClient
                .get()
                .uri("/user/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                    Assertions.assertEquals(userId, response.getResponseBody().getId());
                });
    }

    @Test
    @Order(3)
    public void updateUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user11");
        webTestClient
                .put()
                .uri("/user/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }

    @Test
    @Order(4)
    public void deleteUserTest() {
        webTestClient
                .delete()
                .uri("/user/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }
}
