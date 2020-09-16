package repository;

import domain.Group;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GroupRepository extends ReactiveCrudRepository<Group, Long> {
    Mono<Group> findByTitle(String title);
    Flux<Group> findByTitleContaining(String title);
}
