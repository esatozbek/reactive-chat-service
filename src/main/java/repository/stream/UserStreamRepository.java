package repository.stream;

import domain.User;
import reactor.core.publisher.Flux;

public interface UserStreamRepository {
    Flux<User> getUserStream();
}
