package repository.reactive;

import domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("select * from users where username = :username")
    Mono<User> findByUsername(@Param("username") String username);

    @Query("select * from users where username like :username")
    Flux<User> findByUsernameContaining(@Param("username") String username);

    @Query("LISTEN some_channel;")
    Flux<User> listenUsers();
}
