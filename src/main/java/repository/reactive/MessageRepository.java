package repository.reactive;

import domain.Message;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {
    @Query("select * from message  where content like :content")
    Flux<Message> findMessagesByContentContaining(@Param("content") String content);

    @Query("select * from message  where timestamp between :start and :end")
    Flux<Message> findMessagesByTimestampIsBetween(@Param("start") Long start, @Param("end") Long end);

    @Query("select * from message  where sender_id = :content")
    Flux<Message> findMessagesBySenderId(@Param("senderId") Long senderId);

    @Query("select * from message  where receiver_id = :receiverId")
    Flux<Message> findMessagesByReceiverId(@Param("receiverId") Long receiverId);

    @Query("select * from message  where group_id = :groupId")
    Flux<Message> findMessagesByGroupId(@Param("groupId") Long groupId);

    @Query("select * from message  where receiver_id = :userId or sender_id = :userId order by timestamp desc")
    Flux<Message> findMessagesByReceiverIdOrSenderId(@Param("userId") Long userId);
}
