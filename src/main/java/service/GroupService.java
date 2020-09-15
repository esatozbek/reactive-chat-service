package service;

import domain.Group;
import dto.GroupDTO;
import exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import repository.GroupRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupService {
    private GroupRepository repository;

    public Long create(GroupDTO dto) {
        return repository.save(new Group(dto)).getId();
    }

    public GroupDTO findById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        Group foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new EntityNotFoundException("Group", id);
        return foundEntity.toDTO();
    }

    public Long update(Long id, GroupDTO dto) {
        Group foundEntity = repository.findById(id).orElse(null);
        foundEntity.updateEntity(dto);
        return repository.save(foundEntity).getId();
    }

    public Long delete(Long id) {
        Group foundEntity = repository.findById(id).orElse(null);
        if (Objects.isNull(foundEntity))
            throw new EntityNotFoundException("Group", id);
        repository.delete(foundEntity);
        return id;
    }

    public List<GroupDTO> findAll() {
        return repository.findAll().stream().map(item -> item.toDTO()).collect(Collectors.toList());
    }

    public GroupDTO findGroupByTitle(String title) {
        return repository.findByTitle(title).toDTO();
    }

    public List<GroupDTO> findGroupByTitleContaining(String title) {
        return repository.findByTitleContaining(title).stream().map(item -> item.toDTO()).collect(Collectors.toList());
    }
}
