package service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Group;
import domain.Message;
import domain.User;
import dto.MessageDTO;
import enums.MessageStatus;
import org.springframework.stereotype.Service;
import repository.MessageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MessageService extends CrudTemplate<Message, MessageRepository> {
    @PersistenceContext
    private EntityManager entityManager;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public List<Message> findMessages(MessageDTO dto) {
        return repository.findMessagesByParams(dto.getContent(), dto.getStatus(), dto.getTimestamp(), dto.getSenderId(), dto.getReceiverId(), dto.getGroupId());
    }

    public Message fetchMessageEntity(MessageDTO dto) {
        Message message = new Message();
        message.setContent(dto.getContent());
        message.setStatus(dto.getStatus());
        message.setTimestamp(dto.getTimestamp());
        if (!Objects.isNull(dto.getSenderId())) {
            message.setSender(entityManager.getReference(User.class, dto.getSenderId()));
        }
        if (!Objects.isNull(dto.getReceiverId())) {
            message.setReceiver(entityManager.getReference(User.class, dto.getReceiverId()));
        }
        if (!Objects.isNull(dto.getGroupId())) {
            message.setGroup(entityManager.getReference(Group.class, dto.getGroupId()));
        }
        return message;
    }
}
