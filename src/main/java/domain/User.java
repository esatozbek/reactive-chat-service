package domain;

import dto.UserDTO;
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

    public User(UserDTO dto) {
        this.username = dto.getUsername();
    }

    public void updateEntity(UserDTO newUser) {
        this.username = newUser.getUsername();
    }

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(this.getId());
        dto.setUsername(this.getUsername());
        return dto;
    }
}
