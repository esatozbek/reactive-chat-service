package domain;

import enums.MessageStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("message")
public class Message extends BaseEntity {
    @Column("content")
    private String content;

    @Column("status")
    private MessageStatus status;

    @Column("timestamp")
    private Long timestamp;

    @Column("sender_id")
    private Long senderId;

    @Column("receiver_id")
    private Long receiverId;

    @Column("group_id")
    private Long groupId;
}
