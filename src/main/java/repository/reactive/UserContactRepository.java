package repository.reactive;

import domain.UserContact;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserContactRepository {
    @Query("select * from user_contact where user_id = :userId")
    Flux<UserContact> findContactsFromUser(@Param("userId") Long userId);

    Mono<UserContact> save(UserContact userContact);

    Mono<Integer> delete(Long userId, Long contactId);
}
