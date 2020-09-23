package controller;

import dto.GroupDTO;
import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.AddUserGroupRequest;
import response.BaseResponse;
import response.IdResponse;
import service.GroupService;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    @GetMapping("/{id}")
    public Mono<GroupDTO> getGroup(@PathVariable Long id) {
        return groupService.findById(id);
    }

    @GetMapping()
    public Flux<GroupDTO> getAllGroups(String title) {
        if (Objects.isNull(title))
            return groupService.findAll();
        return groupService.findGroupByTitleContaining(title);
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateGroup(@PathVariable Long id, @RequestBody GroupDTO groupDTO) {
        return groupService.update(id, groupDTO)
                .map(IdResponse::new);
    }

    @PostMapping()
    public Mono<IdResponse> createGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.create(groupDTO)
                .map(IdResponse::new);
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteGroup(@PathVariable Long id) {
        return groupService.delete(id)
                .then(Mono.just(new IdResponse(id)));
    }

    @GetMapping("/users/{id}")
    public Flux<UserDTO> getGroupUsers(@PathVariable Long id) {
        return groupService.getGroupUsers(id);
    }

    @PostMapping("/users")
    public Mono<BaseResponse> addUserToGroup(@RequestBody AddUserGroupRequest request) {
        return groupService
                .addUserGroup(request.getUserId(), request.getGroupId())
                .map(item -> new BaseResponse());
    }
}
