package integrationTest.helpers;

import dto.GroupDTO;
import dto.MessageDTO;
import dto.UserDTO;
import enums.MessageStatus;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import request.MessageRequest;
import response.IdResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static integrationTest.UserIT.USER_URI;

@Component
@Getter
public class TestData {
    public List<Long> userIdList = new ArrayList<>();
    private List<Long> groupIdList = new ArrayList<>();
    private List<Long> messageIdList = new ArrayList<>();

    public static UserDTO prepareUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user1");
        return userDTO;
    }

    public static GroupDTO prepareGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setTitle("title1");
        return groupDTO;
    }

    public static MessageDTO prepareMessageDTO(Long senderId, Long receiverId) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent("content");
        messageDTO.setTimestamp(System.currentTimeMillis());
        messageDTO.setStatus(MessageStatus.SENT);

        UserDTO senderDTO = new UserDTO();
        senderDTO.setId(senderId);
        messageDTO.setSender(senderDTO);

        UserDTO receiverDTO = new UserDTO();
        receiverDTO.setId(receiverId);
        messageDTO.setReceiver(receiverDTO);

        return messageDTO;
    }

    public static MessageRequest prepareMessageRequest(Long senderId, Long receiverId) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setContent("content");
        messageRequest.setTimestamp(System.currentTimeMillis());
        messageRequest.setStatus(MessageStatus.SENT);
        messageRequest.setSenderId(senderId);
        messageRequest.setReceiverId(receiverId);
        return messageRequest;
    }

    public void prepareUserTestData(WebTestClient webTestClient) {
        if (this.userIdList.size() == 10) return;

        for (int i = 0;i < 10;i++) {
            UserDTO userDTO = prepareUserDTO();
            userDTO.setUsername("user" + i);
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

            this.userIdList.add(response.getId());
        }
    }

    public void prepareGroupTestData(WebTestClient webTestClient) {
        if (this.groupIdList.size() == 10) return;

        for (int i = 0;i < 10;i++) {
            GroupDTO groupDTO = prepareGroupDTO();
            groupDTO.setTitle("title" + i);
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

            this.groupIdList.add(response.getId());
        }
    }

    public void prepareMessageTestData(WebTestClient webTestClient) {
        if (this.messageIdList.size() == 10) return;

        Random random = new Random();

        for (int i = 0;i < 0;i++) {
            Long senderId = userIdList.get(random.nextInt(userIdList.size()));
            Long receiverId;
            do {
                receiverId = userIdList.get(random.nextInt(userIdList.size()));
            } while (senderId.equals(receiverId));

            MessageRequest request = TestData.prepareMessageRequest(
                    senderId,
                    receiverId
            );

            IdResponse response = webTestClient
                    .post()
                    .uri("/message")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(IdResponse.class)
                    .returnResult()
                    .getResponseBody();

            this.messageIdList.add(response.getId());
        }
    }
}
