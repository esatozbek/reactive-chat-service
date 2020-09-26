package domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("sender_id")
    private Long senderId;

    @Column("RECEIVER_ID")
    @JsonProperty("receiver_id")
    private Long receiverId;

    @Column("GROUP_ID")
    @JsonProperty("group_id")
    private Long groupId;
}
