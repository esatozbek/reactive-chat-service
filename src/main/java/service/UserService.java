package service;

import domain.User;
import dto.UserDTO;
import exception.EntityNotFoundException;
import exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository repository;

    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public Long createUser(UserDTO userDTO) {
        return repository.save(new User(userDTO)).getId();
    }

    public UserDTO findById(Long id) {
        if (Objects.isNull(id)) {
            throw new InvalidParameterException("user id");
        }
        User foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new EntityNotFoundException("User", id);
        return foundEntity.toDTO();
    }

    public Long update(Long id, UserDTO entity) {
        User foundEntity = repository.findById(id).orElse(null);
        foundEntity.updateEntity(entity);
        return repository.save(foundEntity).getId();
    }

    public Long delete(Long id) {
        User entity = repository.findById(id).orElse(null);
        if (Objects.isNull(entity))
            throw new NullPointerException();
        repository.delete(entity);
        return id;
    }

    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(item -> item.toDTO()).collect(Collectors.toList());
    }

    public UserDTO findUserByUsername(String username) {
        return repository.findByUsername(username).toDTO();
    }

    public List<UserDTO> findUserByUsernameContaining(String username) {
        return repository.findByUsernameContaining(username).stream().map(item -> item.toDTO()).collect(Collectors.toList());
    }
}
