package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.MessageDTO;
import enums.MessageStatus;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@Entity
public class Message extends BaseEntity<Message> {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void updateEntity(Message nMessage) {
        if (!Objects.isNull(nMessage.getContent())) this.content = nMessage.getContent();
        if (!Objects.isNull(nMessage.getStatus())) this.status = nMessage.getStatus();
        if (!Objects.isNull(nMessage.getTimestamp())) this.timestamp = nMessage.getTimestamp();
        if (!Objects.isNull(nMessage.getSender())) this.sender = nMessage.getSender();
        if (!Objects.isNull(nMessage.getReceiver())) this.receiver = nMessage.getReceiver();
        if (!Objects.isNull(nMessage.getGroup())) this.group = nMessage.getGroup();
    }

    public static Message mapFromArguments(Map<String, Object> arguments) {
        return mapFromArguments(arguments, Message.class);
    }
}
