package controller;

import dto.GroupDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import response.IdResponse;
import service.GroupService;

@AllArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    @GetMapping("/{id}")
    public Mono<GroupDTO> getGroup(@PathVariable Long id) {
        return Mono.just(groupService.findById(id));
    }

    @GetMapping()
    public Flux<GroupDTO> getAllGroups() {
        return Flux.fromIterable(groupService.findAll());
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateGroup(@PathVariable Long id, @RequestBody GroupDTO groupDTO) {
        return Mono.just(new IdResponse(groupService.update(id, groupDTO)));
    }

    @PostMapping()
    public Mono<IdResponse> createGroup(@RequestBody GroupDTO groupDTO) {
        return Mono.just(new IdResponse(groupService.create(groupDTO)));
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteGroup(@PathVariable Long id) {
        return Mono.just(new IdResponse(groupService.delete(id)));
    }
}
