package domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User extends BaseEntity<User> {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> group) {
        this.groups = group;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public void addContact(User contact) {
        this.contacts.add(contact);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    @Override
    public void updateEntity(User newUser) {
        this.username = newUser.getUsername();
    }

    public static User mapFromArguments(Map<String, Object> arguments) {
        return mapFromArguments(arguments, User.class);
    }
}
