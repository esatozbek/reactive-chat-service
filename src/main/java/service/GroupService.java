package service;

import domain.BaseEntity;
import domain.Group;
import dto.GroupDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.GroupRepository;

@Service
public class GroupService {
    private GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public Mono<Long> create(GroupDTO dto) {
        return repository.save(new Group(dto)).map(BaseEntity::getId);
    }

    public Mono<GroupDTO> findById(Long id) {
        return repository.findById(id)
                .map(Group::toDTO);
    }

    public Mono<Long> update(Long id, GroupDTO dto) {
        return repository.findById(id)
                .doOnNext(group -> group.updateEntity(dto))
                .map(group -> group.getId());
    }

    public Mono<Long> delete(Long id) {
        return repository.findById(id)
                .doOnNext(group -> repository.delete(group))
                .map(BaseEntity::getId);
    }

    public Flux<GroupDTO> findAll() {
        return repository.findAll().map(Group::toDTO);
    }

    public Mono<GroupDTO> findGroupByTitle(String title) {
        return repository.findByTitle(title)
                .map(Group::toDTO);
    }

    public Flux<GroupDTO> findGroupByTitleContaining(String title) {
        return repository.findByTitleContaining(title).map(item -> item.toDTO());
    }
}
