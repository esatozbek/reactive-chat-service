package repository;

import domain.UserContact;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserContactRepository extends ReactiveCrudRepository<UserContact, Long> {
    @Query("select * from user_contact where user_id = :userId")
    Flux<UserContact> findContactsFromUser(@Param("userId") Long userId);
}
