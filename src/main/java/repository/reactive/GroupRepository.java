package repository.reactive;

import domain.Group;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GroupRepository extends ReactiveCrudRepository<Group, Long> {
    @Query("select * from groups where title = :title")
    Mono<Group> findByTitle(@Param("title") String title);

    @Query("select * from groups where title like :title")
    Flux<Group> findByTitleContaining(@Param("title") String title);
}
