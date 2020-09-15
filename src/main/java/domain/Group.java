package domain;

import dto.GroupDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group extends BaseEntity {
    @Column(name = "title")
    private String title;

    @ManyToMany(targetEntity = User.class)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> groupUsers;

    public Group(GroupDTO dto) {
        this.title = dto.getTitle();
    }

    public GroupDTO toDTO() {
        GroupDTO dto = new GroupDTO();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        return dto;
    }

    public void updateEntity(GroupDTO dto) {
        this.title = dto.getTitle();
    }
}
