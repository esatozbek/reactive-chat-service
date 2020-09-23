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
    @Column("CONTENT")
    private String content;

    @Column("STATUS")
    private MessageStatus status;

    @Column("TIMESTAMP")
    private Long timestamp;

    @Column("SENDER_ID")
    private Long senderId;

    @Column("RECEIVER_ID")
    private Long receiverId;

    @Column("GROUP_ID")
    private Long groupId;
}
