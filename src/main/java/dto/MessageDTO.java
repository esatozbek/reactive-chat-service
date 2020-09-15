package dto;

import enums.MessageStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO implements BaseDTO {
    private Long id;
    private String content;
    private MessageStatus status;
    private Long timestamp;
    private Long senderId;
    private Long receiverId;
    private Long groupId;
}
