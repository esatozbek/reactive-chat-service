package request;

import enums.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageRequest {
    private Long id;
    private String content;
    private MessageStatus status = MessageStatus.SENT;
    private Long timestamp;
    private Long senderId;
    private Long receiverId;
    private Long groupId;
}
