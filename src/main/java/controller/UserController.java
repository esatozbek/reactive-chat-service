package controller;

import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import response.IdResponse;
import service.UserService;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @GetMapping("/{id}")
    public Mono<UserDTO> getUser(@PathVariable Long id) {
        return Mono.just(userService.findById(id));
    }

    @GetMapping()
    public Flux<UserDTO> findUsers(String username) {
        if (Objects.isNull(username))
            return Flux.fromIterable(userService.findAll());
        return Flux.fromIterable(userService.findUserByUsernameContaining(username));
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return Mono.just(new IdResponse(userService.update(id, userDTO)));
    }

    @PostMapping()
    public Mono<IdResponse> createUser(@RequestBody UserDTO userDTO) {
        return Mono.just(new IdResponse(userService.createUser(userDTO)));
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteUser(@PathVariable Long id) {
        return Mono.just(new IdResponse(userService.delete(id)));
    }
}
