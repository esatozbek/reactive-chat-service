package service;

import domain.BaseEntity;
import domain.User;
import dto.UserDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.UserRepository;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repository;

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

    public Mono<Long> delete(Long id) {
        return repository.findById(id)
                .doOnNext(user -> repository.delete(user))
                .map(BaseEntity::getId);
    }

    public Flux<UserDTO> findAll() {
        return repository.findAll().map(item -> item.toDTO());
    }

    public Mono<UserDTO> findUserByUsername(String username) {
        return repository.findByUsername(username)
                .map(User::toDTO);
    }

    public Flux<UserDTO> findUserByUsernameContaining(String username) {
        return repository.findByUsernameContaining(username).map(User::toDTO);
    }
}
