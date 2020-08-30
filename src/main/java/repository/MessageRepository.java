package repository;

import domain.Message;
import enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where (m.content like :content or :content is null) and" +
            " m.status = :status or :status is null and" +
            " m.timestamp = :timestamp or :timestamp is null and" +
            " m.sender.id = :senderId or :senderId is null and" +
            " m.receiver.id = :receiverId or :receiverId is null and" +
            " m.group.id = :groupId or :groupId is null")
    List<Message> findMessagesByParams(@Param("content") String content,
                                       @Param("status") MessageStatus status,
                                       @Param("timestamp") Long timestamp,
                                       @Param("senderId") Long senderId,
                                       @Param("receiverId") Long receiverId,
                                       @Param("groupId") Long groupId);
}
