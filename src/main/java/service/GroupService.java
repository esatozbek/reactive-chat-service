package service;

import domain.BaseEntity;
import domain.Group;
import domain.User;
import domain.UserGroup;
import dto.GroupDTO;
import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.GroupRepository;
import repository.UserGroupRepository;
import repository.UserRepository;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository repository;
    private UserGroupRepository userGroupRepository;
    private UserRepository userRepository;

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
                .map(BaseEntity::getId);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    public Flux<GroupDTO> findAll() {
        return repository.findAll().map(Group::toDTO);
    }

    public Mono<GroupDTO> findGroupByTitle(String title) {
        return repository.findByTitle(title)
                .map(Group::toDTO);
    }

    public Flux<GroupDTO> findGroupByTitleContaining(String title) {
        String wildcardParam = "%" + title + "%";
        return repository.findByTitleContaining(wildcardParam).map(Group::toDTO);
    }

    public Flux<UserDTO> getGroupUsers(Long groupId) {
        return userGroupRepository
                .findUserGroupByUserId(groupId)
                .flatMap(item -> userRepository.findById(item.getUserId()))
                .map(User::toDTO);
    }

    public Mono<UserGroup> addUserGroup(Long userId, Long groupId) {
        UserGroup userGroup = new UserGroup(userId, groupId);

        return userGroupRepository.save(userGroup);
    }
}
