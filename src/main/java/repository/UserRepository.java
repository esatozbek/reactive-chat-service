package repository;

import domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("select u from users u where u.sender_id = :senderId")
    Mono<User> findByUsername(String username);

    @Query("select u from users u where u.sender_id = :senderId")
    Flux<User> findByUsernameContaining(String username);
}
