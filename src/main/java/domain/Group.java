package domain;

import dto.GroupDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("groups")
public class Group extends BaseEntity {
    @Column("TITLE")
    private String title;

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
