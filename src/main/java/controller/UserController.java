package controller;

import domain.User;
import dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.AddContactRequest;
import response.BaseResponse;
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

    @GetMapping("/contacts")
    public Flux<UserDTO> getContactsFromUserId(@RequestHeader("x-user-id") Long userId) {
        return userService.getContactsFromUser(userId);
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<UserDTO> listenUsers() {
        return userService.getUserStream();
    }
}
