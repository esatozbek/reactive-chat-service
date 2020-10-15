package domain;

import dto.UserDTO;
import enums.UserStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("users")
public class User extends BaseEntity {
    @Column("username")
    private String username;

    @Column("status")
    private UserStatusEnum status;

    public User(UserDTO dto) {
        this.username = dto.getUsername();
        this.status = dto.getStatus();
    }

    public void updateEntity(UserDTO newUser) {
        this.username = newUser.getUsername();
    }

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(this.getId());
        dto.setUsername(this.getUsername());
        dto.setStatus(this.getStatus());
        return dto;
    }
}
