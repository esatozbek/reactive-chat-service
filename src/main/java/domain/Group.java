package domain;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "groups")
public class Group extends BaseEntity<Group> {
    @Column(name = "title")
    private String title;

    @ManyToMany(targetEntity = User.class)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> groupUsers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<User> groupUsers) {
        this.groupUsers = groupUsers;
    }

    @Override
    public void updateEntity(Group newGroup) {
        this.title = newGroup.getTitle();
    }

    public static Group mapFromArguments(Map<String, Object> arguments) {
        return mapFromArguments(arguments, Group.class);
    }
}
