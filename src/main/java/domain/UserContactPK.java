package domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class UserContactPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column("USER_ID")
    private Long userId;

    @Id
    @Column("CONTACT_ID")
    private Long contactId;
}
