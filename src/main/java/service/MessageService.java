package service;

import domain.BaseEntity;
import domain.Message;
import dto.MessageDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.MessageRepository;
import request.MessageRequest;

import java.util.Objects;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository repository;
    private UserService userService;
    private GroupService groupService;

    public void updateMessage(Message message, MessageRequest request) {
        if (!Objects.isNull(request.getReceiverId()) && !Objects.isNull(request.getGroupId()))
            throw new InvalidParameterException("Receiver user and group can not be present at the same time.");
        else if (Objects.isNull(request.getReceiverId()) && Objects.isNull(request.getGroupId()))
            throw new InvalidParameterException("One of receiver user and group must be present.");

        message.setContent(request.getContent());
        message.setStatus(request.getStatus());
        message.setTimestamp(request.getTimestamp());

        userService.findById(request.getSenderId())
                .doOnNext(user -> message.setSenderId(user.getId()))
                .doOnError(e -> Mono.error(new EntityNotFoundException("Sender user", request.getSenderId())))
                .subscribe();

        if (!Objects.isNull(request.getReceiverId())) {
            userService.findById(request.getReceiverId())
                    .doOnNext(user -> message.setReceiverId(user.getId()))
                    .doOnError(e -> Mono.error(new EntityNotFoundException("Receiver user", request.getSenderId())))
                    .subscribe();
        }

        if (!Objects.isNull(request.getGroupId())) {
            groupService.findById(request.getGroupId())
                    .doOnNext(group -> message.setGroupId(group.getId()))
                    .doOnError(e -> Mono.error(new EntityNotFoundException("Group", request.getSenderId())))
                    .subscribe();
        }
    }

    public MessageDTO toDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setStatus(message.getStatus());
        dto.setTimestamp(message.getTimestamp());

        userService.findById(message.getSenderId())
                .doOnNext(dto::setSender)
                .doOnError(e -> Mono.error(new EntityNotFoundException("Receiver user", message.getReceiverId())))
                .subscribe();

        if (!Objects.isNull(message.getReceiverId())) {
            userService.findById(message.getReceiverId())
                    .doOnNext(dto::setReceiver)
                    .doOnError(e -> Mono.error(new EntityNotFoundException("Receiver user", message.getReceiverId())))
                    .subscribe();
        }
        if (!Objects.isNull(message.getGroupId())) {
            groupService.findById(message.getGroupId())
                    .doOnNext(dto::setGroup)
                    .doOnError(e -> Mono.error(new EntityNotFoundException("Group", message.getSenderId())))
                    .subscribe();
        }

        return dto;
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

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    public Mono<MessageDTO> findById(Long id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    public Flux<MessageDTO> findAll() {
        return repository.findAll().map(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByContent(String content) {
        return repository.findMessagesByContentContaining(content)
                .map(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesBetweenTimestamps(Long start, Long end) {
        return repository.findMessagesByTimestampIsBetween(start, end)
                .map(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesBySender(Long senderId) {
        return repository.findMessagesBySenderId(senderId)
                .map(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByReceiver(Long receiverId) {
        return repository.findMessagesByReceiverId(receiverId)
                .map(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByGroupId(Long groupId) {
        return repository.findMessagesByGroupId(groupId)
                .map(this::toDTO);
    }
}
