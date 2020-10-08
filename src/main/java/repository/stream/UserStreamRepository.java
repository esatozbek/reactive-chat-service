package repository.stream;

import domain.User;
import domain.UserContact;
import reactor.core.publisher.Flux;

public interface UserStreamRepository {
    Flux<User> getUserStream();
    Flux<UserContact> getContactStream();
}
