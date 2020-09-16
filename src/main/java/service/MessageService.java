package service;

import domain.BaseEntity;
import domain.Group;
import domain.Message;
import domain.User;
import dto.MessageDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.GroupRepository;
import repository.MessageRepository;
import repository.UserRepository;
import request.MessageRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository repository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;

    public void updateMessage(Message message, MessageRequest request) {
        message.setContent(request.getContent());
        message.setStatus(request.getStatus());
        message.setTimestamp(request.getTimestamp());

        User senderUser = userRepository.findById(request.getSenderId());
        if (Objects.isNull(senderUser))
            throw new EntityNotFoundException("Sender user", request.getSenderId());
        message.setSender(senderUser);

        if (!Objects.isNull(request.getReceiverId()) && !Objects.isNull(request.getGroupId()))
            throw new InvalidParameterException("Receiver user and group can not be present at the same time.");
        else if (Objects.isNull(request.getReceiverId()) && Objects.isNull(request.getGroupId()))
            throw new InvalidParameterException("One of receiver user and group must be present.");

        if (!Objects.isNull(request.getReceiverId())) {
            User receiverUser = userRepository.findById(request.getSenderId());
            if (Objects.isNull(receiverUser))
                throw new EntityNotFoundException("Sender user", request.getSenderId());
            message.setReceiver(receiverUser);
        }

        if (!Objects.isNull(request.getGroupId())) {
            Group group = groupRepository.findById(request.getGroupId());
            if (Objects.isNull(group))
                throw new EntityNotFoundException("Group", request.getGroupId());
            message.setGroup(group);
        }
    }

    public Mono<Long> create(MessageRequest request) {
        Message newMessage = new Message();
        updateMessage(newMessage, request);

        return repository.save(newMessage).map(message -> message.getId());
    }

    public Mono<Long> update(Long id, MessageRequest request) {
        return repository.findById(id)
                .doOnNext(message -> updateMessage(message, request))
                .map(BaseEntity::getId);
    }

    public Mono<Long> delete(Long id) {
        return repository.findById(id)
                .doOnNext(message -> repository.delete(message))
                .map(BaseEntity::getId);
    }

    public Mono<MessageDTO> findById(Long id) {
        return repository.findById(id)
                .map(Message::toDTO);
    }

    public Flux<MessageDTO> findAll() {
        return repository.findAll().map(Message::toDTO);
    }

    public Flux<MessageDTO> findMessagesByContent(String content) {
        return repository.findMessagesByContentContaining(content)
                .map(Message::toDTO);
    }

    public Flux<MessageDTO> findMessagesBetweenTimestamps(Long start, Long end) {
        return repository.findMessagesByTimestampIsBetween(start, end)
                .map(Message::toDTO);
    }

    public Flux<MessageDTO> findMessagesBySender(Long senderId) {
        return repository.findMessagesBySenderId(senderId)
                .map(Message::toDTO);
    }

    public Flux<MessageDTO> findMessagesByReceiver(Long receiverId) {
        return repository.findMessagesByReceiverId(receiverId)
                .map(Message::toDTO);
    }

    public Flux<MessageDTO> findMessagesByGroupId(Long groupId) {
        return repository.findMessagesByGroupId(groupId)
                .map(Message::toDTO);
    }
}
