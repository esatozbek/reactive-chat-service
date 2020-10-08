package service;

import domain.BaseEntity;
import domain.Message;
import domain.User;
import domain.UserContact;
import dto.UserDTO;
import exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.reactive.MessageRepository;
import repository.reactive.UserContactRepository;
import repository.reactive.UserRepository;
import repository.stream.UserStreamRepository;

@Service
public class UserService {
    private UserRepository repository;
    private UserContactRepository userContactRepository;
    private UserStreamRepository streamRepository;
    private MessageRepository messageRepository;

    public UserService(UserRepository repository, UserContactRepository userContactRepository, UserStreamRepository userStreamRepository, MessageRepository messageRepository) {
        this.repository = repository;
        this.userContactRepository = userContactRepository;
        this.streamRepository = userStreamRepository;
        this.messageRepository = messageRepository;
    }

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

    public Flux<UserDTO> findAll() {
        return repository.findAll().map(item -> item.toDTO());
    }

    public Mono<UserDTO> findUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(User::toDTO);
    }

    public Flux<UserDTO> findUserByUsernameContaining(String username) {
        String percentageConcated = "%" + username + "%";
        return repository.findByUsernameContaining(percentageConcated).map(User::toDTO);
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
                .map(message -> message.getSenderId().equals(userId) ? message.getReceiverId() : message.getSenderId())
                .distinct()
                .flatMap(this::findById);
    }
}
