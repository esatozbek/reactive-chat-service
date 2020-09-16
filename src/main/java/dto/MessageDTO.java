package dto;

import enums.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDTO implements BaseDTO {
    private Long id;
    private String content;
    private MessageStatus status;
    private Long timestamp;
    private UserDTO sender;
    private UserDTO receiver;
    private GroupDTO group;
}
