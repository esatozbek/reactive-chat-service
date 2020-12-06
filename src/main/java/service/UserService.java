package service;

import domain.BaseEntity;
import domain.Message;
import domain.User;
import domain.UserContact;
import dto.UserDTO;
import enums.UserStatusEnum;
import exception.EntityNotFoundException;
import exception.LoginException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.reactive.MessageRepository;
import repository.reactive.UserContactRepository;
import repository.reactive.UserRepository;
import repository.stream.UserStreamRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository repository;
    private UserContactRepository userContactRepository;
    private UserStreamRepository streamRepository;
    private MessageRepository messageRepository;

    public Mono<Long> createUser(UserDTO userDTO) {
        return repository.save(new User(userDTO)).map(BaseEntity::getId);
    }

    public Mono<UserDTO> findById(Long id) {
        return repository.findById(id)
                .map(User::toDTO)
                .onErrorMap(e -> new EntityNotFoundException("User", id));
    }

    public Mono<Long> update(Long id, UserDTO dto) {
        return repository.findById(id)
                .doOnNext(user -> user.updateEntity(dto))
                .map(BaseEntity::getId);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    public Mono<UserDTO> login(String username) {
        return repository
                .findByUsername(username)
                .defaultIfEmpty(new User())
                .flatMap(user -> {
                    if (Objects.isNull((user.getId()))) {
                        return Mono.error(new LoginException());
                    }
                    user.setStatus(UserStatusEnum.ONLINE);
                    return repository.save(user);
                })
                .map(User::toDTO)
                .log();
    }

    public Mono<UserDTO> logout(Long id) {
        return repository.findById(id).flatMap(user -> {
            user.setStatus(UserStatusEnum.OFFLINE);
            return repository.save(user);
        }).map(User::toDTO);
    }

    public Flux<UserDTO> findAll() {
        return repository.findAll().map(item -> item.toDTO());
    }

    public Mono<UserDTO> findUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(User::toDTO);
    }

    public Flux<UserDTO> findUserByUsernameContaining(String username) {
        String percentageConcatenated = "%" + username + "%";
        return repository.findByUsernameContaining(percentageConcatenated).map(User::toDTO);
    }

    public Mono<UserContact> addContact(Long userId, Long contactId) {
        UserContact userContact = new UserContact(userId, contactId);
        return userContactRepository.save(userContact);
    }

    public Flux<UserDTO> getContactsFromUser(Long userId) {
        return userContactRepository
                .findContactsFromUser(userId)
                .flatMap(userContact -> repository.findById(userContact.getContactId()))
                .map(User::toDTO);
    }

    public Flux<UserDTO> getUserStream() {
        return streamRepository.getUserStream().map(User::toDTO);
    }

    public Flux<UserDTO> getContactsStream(Long userId) {
        return streamRepository
                .getContactStream()
                .filter(item -> item.getUserId().equals(userId))
                .flatMap(item -> findById(item.getContactId()));
    }

    public Flux<UserDTO> getRecentChatUsers(Long userId) {
        Flux<Message> myMessages = messageRepository.findMessagesByReceiverIdOrSenderId(userId);

        return myMessages
                // .sort((m1, m2) -> m2.getTimestamp().compareTo(m1.getTimestamp()))
                .map(message -> {
                    log.info(new Date(message.getTimestamp()).toString());
                    return message.getSenderId().equals(userId) ? message.getReceiverId() : message.getSenderId();
                })
                .reduce(new LinkedHashSet<Long>(), (set, oppositeUserId) -> {
                    log.info(String.valueOf(oppositeUserId));
                    set.add(oppositeUserId);
                    return set;
                })
                .flatMapMany(set -> Flux.fromStream(set.stream()))
                .flatMap(this::findById);
    }
}
