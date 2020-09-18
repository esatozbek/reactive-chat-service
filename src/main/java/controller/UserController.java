package controller;

import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import response.IdResponse;
import service.UserService;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    private ApplicationContext context;

    @GetMapping("/{id}")
    public Mono<UserDTO> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping()
    public Flux<UserDTO> findUsers(String username) {
        System.out.println(Arrays.toString(context.getEnvironment().getActiveProfiles()));
        if (Objects.isNull(username))
            return userService.findAll();
        return userService.findUserByUsernameContaining(username);
    }

    @PutMapping("/{id}")
    public Mono<IdResponse> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO)
                .map(IdResponse::new);
    }

    @PostMapping()
    public Mono<IdResponse> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO)
                .map(IdResponse::new);
    }

    @DeleteMapping("/{id}")
    public Mono<IdResponse> deleteUser(@PathVariable Long id) {
        return userService.delete(id)
                .map(IdResponse::new);
    }
}
