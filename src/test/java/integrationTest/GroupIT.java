package integrationTest;

import config.Application;
import dto.GroupDTO;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { Application.class, TestData.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GroupIT {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    TestData testData;

    private final static String GROUP_URI = "/group";

    private static Long groupId;

    @Test
    @Order(1)
    public void createGroupTest() {
        GroupDTO groupDTO = TestData.prepareGroupDTO();

        IdResponse response = webTestClient
                .post()
                .uri(GROUP_URI)
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
                .uri(GROUP_URI + "/" + groupId)
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
        GroupDTO groupDTO = TestData.prepareGroupDTO();
        groupDTO.setTitle("different title");

        webTestClient
                .put()
                .uri(GROUP_URI + "/" + groupId)
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
                .uri(GROUP_URI + "/" + groupId)
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
    public void getGroupsTest() {
        testData.prepareGroupTestData(webTestClient);

        webTestClient
                .get()
                .uri(GROUP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GroupDTO.class)
                .hasSize(testData.getGroupIdList().size());
    }

    @Test
    @Order(6)
    public void getGroupsByTitleTest() {
        webTestClient
                .get()
                .uri(GROUP_URI + "?title=title")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GroupDTO.class)
                .hasSize(testData.getGroupIdList().size());
    }

    @Test
    @Order(7)
    public void getGroupsByTitleEmptyTest() {
        webTestClient
                .get()
                .uri(GROUP_URI + "?title=test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GroupDTO.class)
                .hasSize(0);
    }

    @Test
    @Order(8)
    public void createGroupNullTest() {
        GroupDTO groupDTO = TestData.prepareGroupDTO();
        groupDTO.setTitle(null);

        webTestClient
                .post()
                .uri(GROUP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(groupDTO)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
