package service;

import domain.Group;
import org.springframework.stereotype.Service;
import repository.GroupRepository;

import java.util.List;
import java.util.Objects;

@Service
public class GroupService extends CrudTemplate<Group, GroupRepository> {
    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public Group findGroupByTitle(String title) {
        return repository.findByTitle(title);
    }

    public List<Group> findGroupByTitleContaining(String title) {
        return repository.findByTitleContaining(title);
    }
}
