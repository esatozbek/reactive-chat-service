package dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import enums.MessageStatus;

import java.util.Map;

public class MessageDTO implements LightDTO {
    private Long id;
    private String content;
    private MessageStatus status;
    private Long timestamp;
    private Long senderId;
    private Long receiverId;
    private Long groupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public static MessageDTO mapFromArguments(Map<String, Object> arguments) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(arguments, MessageDTO.class);
    }
}
