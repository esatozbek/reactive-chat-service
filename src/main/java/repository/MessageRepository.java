package repository;

import domain.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {
    Flux<Message> findMessagesByContentContaining(String content);

    Flux<Message> findMessagesByTimestampIsBetween(Long start, Long end);

    @Query("select m from Message m where m.sender.id = :senderId")
    Flux<Message> findMessagesBySenderId(@Param("senderId") Long senderId);

    @Query("select m from Message m where m.receiver.id = :receiverId")
    Flux<Message> findMessagesByReceiverId(@Param("receiverId") Long receiverId);

    @Query("select m from Message m where m.group.id = :groupId")
    Flux<Message> findMessagesByGroupId(@Param("groupId") Long groupId);
}
