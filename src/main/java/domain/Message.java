package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.MessageDTO;
import dto.UserDTO;
import enums.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import request.MessageRequest;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Message extends BaseEntity {
    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private MessageStatus status;

    @Column(name = "timestamp")
    private Long timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private Group group;

    public MessageDTO toDTO() {
        MessageDTO dto = new MessageDTO();
        dto.setId(this.id);
        dto.setContent(this.content);
        dto.setStatus(this.status);
        dto.setTimestamp(this.timestamp);
        dto.setSender(this.sender.toDTO());
        if (!Objects.isNull(this.receiver)) dto.setReceiver(this.receiver.toDTO());
        if (!Objects.isNull(this.group)) dto.setGroup(this.group.toDTO());
        return dto;
    }
}
