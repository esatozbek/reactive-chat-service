package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("user_contact")
public class UserContact {
    @Column("USER_ID")
    private Long userId;

    @Column("CONTACT_ID")
    private Long contactId;
}
