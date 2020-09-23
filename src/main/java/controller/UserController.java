package controller;

import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.AddContactRequest;
import response.BaseResponse;
import response.IdResponse;
import response.ParametricResponse;
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

    @GetMapping("/username/{username}")
    public Mono<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping()
    public Flux<UserDTO> findUsers(String username) {
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
                .then(Mono.just(new IdResponse(id)));
    }

    @PostMapping("/contact")
    public Mono<BaseResponse> addContact(@RequestBody AddContactRequest addContactRequest) {
        return userService
                .addContact(addContactRequest.getUserId(), addContactRequest.getContactId())
                .map(item -> new BaseResponse());
    }

    @GetMapping("/contact/{userId}")
    public Flux<UserDTO> getContactsFromUserId(@PathVariable("userId") Long userId) {
        return userService.getContactsFromUser(userId);
    }
}
