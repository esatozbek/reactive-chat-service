package integrationTest;

import config.Application;
import dto.GroupDTO;
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
public class GroupIT {
    @Autowired
    WebTestClient webTestClient;

    private static Long groupId;

    @Test
    @Order(1)
    public void createGroupTest() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setTitle("title1");
        IdResponse response = webTestClient
                .post()
                .uri("/group")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(groupDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertTrue(response.getStatus());
        groupId = response.getId();
    }

    @Test
    @Order(2)
    public void getGroupTest() {
        webTestClient
                .get()
                .uri("/group/" + groupId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GroupDTO.class)
                .consumeWith(response -> {
                    Assertions.assertEquals(groupId, response.getResponseBody().getId());
                });
    }

    @Test
    @Order(3)
    public void updateGroupTest() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setTitle("title11");
        webTestClient
                .put()
                .uri("/group/" + groupId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(groupDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }

    @Test
    @Order(4)
    public void deleteGroupTest() {
        webTestClient
                .delete()
                .uri("/group/" + groupId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponse.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getResponseBody().getStatus());
                });
    }
}
