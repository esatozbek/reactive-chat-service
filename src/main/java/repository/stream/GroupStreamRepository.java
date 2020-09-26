package repository.stream;

import domain.Group;
import reactor.core.publisher.Flux;

public interface GroupStreamRepository {
    Flux<Group> getGroupStream();
}
