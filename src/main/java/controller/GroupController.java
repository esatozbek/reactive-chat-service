package controller;

import dto.GroupDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
        if (!Objects.isNull(title))
            return groupService.findGroupByTitleContaining(title);
        return groupService.findAll();
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
                .map(IdResponse::new);
    }
}
