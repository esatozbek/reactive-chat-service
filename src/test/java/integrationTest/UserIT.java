package integrationTest;

import config.Application;
import dto.UserDTO;
import integrationTest.helpers.TestData;
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
@SpringBootTest(classes = { Application.class, TestData.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIT {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    TestData testData;

    public final static String USER_URI = "/user";

    private static Long userId;

    @Test
    @Order(1)
    public void createUser() {
        UserDTO userDTO = TestData.prepareUserDTO();

        IdResponse response = webTestClient
                .post()
                .uri(USER_URI)
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
                .uri(USER_URI + "/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                    Assertions.assertEquals(userId, response.getResponseBody().getId());
                    Assertions.assertEquals("user1", response.getResponseBody().getUsername());
                });
    }

    @Test
    @Order(3)
    public void updateUserTest() {
        UserDTO userDTO = TestData.prepareUserDTO();
        userDTO.setUsername("user11");

        webTestClient
                .put()
                .uri(USER_URI + "/" + userId)
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
                .uri(USER_URI + "/" + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> Assertions.assertTrue(response.getResponseBody().getStatus()));
    }

    @Test
    @Order(5)
    public void findUsersTest() {
        testData.prepareUserTestData(webTestClient);

        webTestClient
                .get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(testData.getUserIdList().size());
    }

    @Test
    @Order(6)
    public void findUsersByUsernameTest() {
        webTestClient
                .get()
                .uri(USER_URI + "?username=user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(testData.getUserIdList().size());
    }

    @Test
    @Order(7)
    public void findUsersByUsernameEmptyTest() {
        webTestClient
                .get()
                .uri(USER_URI + "?username=test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDTO.class)
                .hasSize(0);
    }

    @Test
    @Order(8)
    public void createUserNullTest() {
        UserDTO userDTO = TestData.prepareUserDTO();
        userDTO.setUsername(null);

        webTestClient
                .post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
