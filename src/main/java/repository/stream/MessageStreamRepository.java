package repository.stream;

import domain.Message;
import reactor.core.publisher.Flux;

public interface MessageStreamRepository {
    Flux<Message> getMessageStream();
}
