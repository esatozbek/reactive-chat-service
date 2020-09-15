package service;

import domain.Group;
import domain.Message;
import domain.User;
import dto.GroupDTO;
import dto.MessageDTO;
import exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import repository.MessageRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private MessageRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Long create(MessageDTO dto) {
        return repository.save(new Message(dto)).getId();
    }

    public MessageDTO findById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        Message foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new EntityNotFoundException("Message", id);
        return foundEntity.toDTO();
    }

    public Long update(Long id, MessageDTO dto) {
        Message foundEntity = repository.findById(id).orElse(null);
        foundEntity.updateEntity(dto);
        return repository.save(foundEntity).getId();
    }

    public Long delete(Long id) {
        Message foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new EntityNotFoundException("Group", id);
        repository.delete(foundEntity);
        return id;
    }

    public List<MessageDTO> findAll() {
        return repository.findAll().stream().map(item -> item.toDTO()).collect(Collectors.toList());
    }

    public List<MessageDTO> findMessages(MessageDTO dto) {
        return repository.findMessagesByParams(dto.getContent(), dto.getStatus(), dto.getTimestamp(), dto.getSenderId(), dto.getReceiverId(), dto.getGroupId()).stream().map(item -> item.toDTO()).collect(Collectors.toList());
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
