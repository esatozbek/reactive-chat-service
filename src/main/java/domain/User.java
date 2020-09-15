package domain;

import dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "username")
    private String username;

    @ManyToMany(mappedBy = "groupUsers")
    private List<Group> groups;

    @ManyToMany
    @JoinTable(
            name = "user_contact",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<User> contacts;

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
