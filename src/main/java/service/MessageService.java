package service;

import domain.BaseEntity;
import domain.Message;
import dto.GroupDTO;
import dto.MessageDTO;
import dto.UserDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.reactive.MessageRepository;
import repository.stream.MessageStreamRepository;
import request.MessageRequest;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private MessageRepository repository;
    private MessageStreamRepository streamRepository;

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

    public Mono<MessageDTO> toDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setStatus(message.getStatus());
        dto.setTimestamp(message.getTimestamp());

        Mono<UserDTO> sender = userService
                .findById(message.getSenderId())
                .doOnNext(dto::setSender);

        Mono<UserDTO> receiver = Mono.empty();
        if (!Objects.isNull(message.getReceiverId()))
            receiver = userService
                .findById(message.getReceiverId())
                .doOnNext(dto::setReceiver);

        Mono<GroupDTO> group = Mono.empty();
        if (!Objects.isNull(message.getGroupId()))
            groupService
                    .findById(message.getGroupId())
                    .doOnNext(dto::setGroup);

        return Flux
                .concat(sender, receiver, group)
                .then(Mono.just(dto));
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
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findAll() {
        return repository.findAll().flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByContent(String content) {
        return repository.findMessagesByContentContaining(content)
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesBetweenTimestamps(Long start, Long end) {
        return repository.findMessagesByTimestampIsBetween(start, end)
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesBySender(Long senderId) {
        return repository.findMessagesBySenderId(senderId)
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByReceiver(Long receiverId) {
        return repository.findMessagesByReceiverId(receiverId)
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> findMessagesByGroupId(Long groupId) {
        return repository.findMessagesByGroupId(groupId)
                .flatMap(this::toDTO);
    }

    public Flux<MessageDTO> getMessageStream() {
        return streamRepository.getMessageStream().flatMap(this::toDTO);
    }
}
