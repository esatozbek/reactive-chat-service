package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.MessageDTO;
import enums.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Message(MessageDTO dto) {
        this.setContent(dto.getContent());
        this.setStatus(dto.getStatus());
        this.setTimestamp(dto.getTimestamp());
//        this.setSender(dto.getSenderId());
//        this.setReceiver(dto.getReceiverId());
//        this.setReceiver(dto.getGroupId());
    }

    public void updateEntity(MessageDTO nMessage) {
        if (!Objects.isNull(nMessage.getContent())) this.content = nMessage.getContent();
        if (!Objects.isNull(nMessage.getStatus())) this.status = nMessage.getStatus();
        if (!Objects.isNull(nMessage.getTimestamp())) this.timestamp = nMessage.getTimestamp();
//        if (!Objects.isNull(nMessage.getSender())) this.sender = nMessage.getSender();
//        if (!Objects.isNull(nMessage.getReceiver())) this.receiver = nMessage.getReceiver();
//        if (!Objects.isNull(nMessage.getGroup())) this.group = nMessage.getGroup();
    }

    public MessageDTO toDTO() {
        MessageDTO dto = new MessageDTO();
        dto.setId(this.id);
        dto.setContent(this.content);
        dto.setStatus(this.status);
        dto.setTimestamp(this.timestamp);
//        dto.setSenderId(this.sender.getId());
//        dto.setReceiverId(this.receiver.getId());
//        dto.setGroupId(this.group.getId());
        return dto;
    }
}
