package service;

import domain.BaseEntity;
import domain.User;
import domain.UserContact;
import dto.UserDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.reactive.UserContactRepository;
import repository.reactive.UserRepository;
import repository.stream.UserStreamRepository;

import java.util.Objects;

@Service
public class UserService {
    private UserRepository repository;
    private UserContactRepository userContactRepository;
    private UserStreamRepository streamRepository;

    public UserService(UserRepository repository, UserContactRepository userContactRepository, UserStreamRepository userStreamRepository) {
        this.repository = repository;
        this.userContactRepository = userContactRepository;
        this.streamRepository = userStreamRepository;
    }

    public Mono<Long> createUser(UserDTO userDTO) {
        return repository.save(new User(userDTO)).map(BaseEntity::getId);
    }

    public Mono<UserDTO> findById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidParameterException("user id");
        }
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
}
