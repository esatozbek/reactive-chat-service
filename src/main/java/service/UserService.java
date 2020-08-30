package service;

import domain.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;

@Service
public class UserService extends CrudTemplate<User, UserRepository> {
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public User findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<User> findUserByUsernameContaining(String username) {
        return repository.findByUsernameContaining(username);
    }
}
