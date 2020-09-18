package repository;

import domain.Group;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GroupRepository extends ReactiveCrudRepository<Group, Long> {
    @Query("select g from groups g where title = $1")
    Mono<Group> findByTitle(String title);

    @Query("select g from groups g where title = $1")
    Flux<Group> findByTitleContaining(String title);
}
